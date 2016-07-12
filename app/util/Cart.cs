using System;
using System.Web;
using System.Net;
using System.Collections.Generic;
using util.cprbroker;
using cpreader.Properties;

namespace util
{
    /**
     * Created by Beemen on 17/5/15.
     */
    public class Cart
    {

        private static readonly int CART_CACHE_TIMEOUT = 3600; // 1 hour

        // Private constructor
        private Cart()
        {

        }

        public static String cacheKey()
        {
            return controllers.Search.getSessionId() + "-cart";
        }

        public static Cart fromSession()
        {
            HttpContext context = HttpContext.Current;
            Cart ret = (Cart)context.Cache[cacheKey()];
            if (ret == null)
            {
                ret = new Cart();
                ret.saveToSession();
            }
            return ret;
        }

        public void saveToSession()
        {
            HttpContext.Current.Cache[cacheKey()] = this;
        }

        public String add(String uuid)
        {
            IPerson person = (IPerson)HttpContext.Current.Cache[uuid];
            if (person != null)
            {
                Cart.Person cartPerson = new Cart.Person(person);
                if (exists(uuid))
                {
                    return String.Format("{0} {1}", cartPerson, Messages.cart_exists);
                }
                else {
                    Persons.Add(cartPerson);
                    return String.Format("{0} {1}", cartPerson, Messages.cart_added);
                }
            }
            else {
                play.Logger.info("problem adding " + uuid);
                return "";
            }
        }

        public bool exists(String uuid)
        {
            for (int i = 0; i < Persons.Count; i++)
            {
                if (uuid.Equals(Persons[i].UUID))
                    return true;
            }
            return false;
        }

        public String remove(String uuid)
        {
            for (int i = 0; i < Persons.Count; i++)
            {
                if (uuid.Equals(Persons[i].UUID))
                {
                    Person cartPerson = Persons[i];
                    Persons.RemoveAt(i);
                    return String.Format("{0} {1}", cartPerson, Messages.cart_removed);
                }
            }
            return String.Format("{0} {1}", uuid, Messages.cart_notremoved);
        }

        public void clear()
        {
            Persons.Clear();
        }

        public readonly List<Person> Persons = new List<Person>();

        public class Person
        {
            public String UUID;
            public String PNR;
            public String firstName;
            public String middleName;
            public String lastName;

            public Person(IPerson person)
            {
                UUID = person.uuid();
                firstName = person.firstname();
                middleName = "";
                lastName = person.lastname();

                if (person.middelname() != null)
                {
                    middleName = person.middelname();
                }
                PNR = person.registerInformation().cprCitizen().socialSecurityNumber();
            }

            public override String ToString()
            {
                return firstName + " " + middleName + " " + lastName;
            }

        }
    }
}