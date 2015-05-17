package util;

import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Http;
import util.cprbroker.IPerson;
import java.util.ArrayList;

/**
 * Created by Beemen on 17/5/15.
 */
public class Cart {

    private static final int CART_CACHE_TIMEOUT = 3600; // 1 hour

    // Private constructor
    private Cart(){

    }

    public static String cacheKey(){
        return controllers.Search.getSessionId() + "-cart";
    }

    public static Cart fromSession(){
        Http.Context context = Http.Context.current();
        Cart ret = (Cart)Cache.get(cacheKey());
        if(ret == null){
            ret = new Cart();
            ret.saveToSession();
        }
        return ret;
    }

    public void saveToSession(){
        Cache.set(cacheKey(), CART_CACHE_TIMEOUT);
    }

    public String add(String uuid){
        IPerson person = (IPerson) Cache.get(uuid);
        if(person != null)
        {
            Cart.Person cartPerson = new Cart.Person(person);
            if(exists(uuid)){
                Persons.add(cartPerson);
                return String.format("%s %s",cartPerson, Messages.get("cart.exists"));
            }
            else{
                return String.format("%s %s",cartPerson, Messages.get("cart.added"));
            }
        }
        else{
            play.Logger.info("problem adding " + uuid);
            return "";
        }
    }

    public boolean exists(String uuid){
        for(int i=0; i<Persons.size(); i++){
            if(uuid.equals(Persons.get(i).UUID))
                return true;
        }
        return false;
    }

    public Boolean remove(String uuid){
        for(int i=0; i<Persons.size(); i++){
            if(uuid.equals(Persons.get(i).UUID))
            {
                Persons.remove(i);
                return true;
            }
        }
        return false;
    }

    public void clear(){
        Persons.clear();
    }

    public final ArrayList<Person> Persons = new ArrayList<>();

    public class Person{
        public String UUID;
        public String PNR;
        public String firstName;
        public String middleName;
        public String lastName;

        public Person(IPerson person){
            UUID = person.uuid();
            firstName=person.firstname();
            middleName="";
            lastName=person.lastname();

            if (person.middelname() != null) {
                middleName = person.middelname();
            }
            PNR = person.registerInformation().cprCitizen().socialSecurityNumber();
        }

        @Override
        public String toString(){
            return firstName + " " + middleName + " " + lastName;
        }

    }
}
