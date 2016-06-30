/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 2.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 * Beemen Beshara
 * Søren Kirkegård
 *
 * The code is currently governed by OS2 - Offentligt digitaliserings-
 * fællesskab / http://www.os2web.dk .
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

using System;
using System.Collections.Generic;
namespace util.cprbroker.models
{
















    public class Person : IPerson
    {

        // basic attributes
        public int _code;
        public String _message;
        public String _uuid;

        public String _firstname;
        public String _middelname;
        public String _lastname;
        public String _callname;
        public String _nameForAddressing;
        public EGenderType _gender;
        public DateTime _birthdate;
        public String _birthplace;
        public String _birthRegisteringAuthority;

        // register information
        public IRegisterInformation _registerInformation;

        // address
        public IAddress _address;
        public String[] _postalLabel;
        public IAddress _otherAddress;

        // contact
        public IContact _contact;
        public IContact _nextOfKinContact;

        // effect
        public IVirkning _effect;

        // relationships
        public IPersonRelationships _relations;
        public IPersonRelationshipsWithIPerson _relationsWithPerson;

        // tilstand
        public ITilstand _tilstand;

        public ITidspunkt _tidspunkt;

        /**
         * Builder for a Person
         *
         * @author Søren Kirkegård
         */
        public class Builder
        {
            //Required parameters
            public int _code;
            public String _message;
            public String _uuid;

            //Optional parameters - initialized to default values
            ////IPersonAttributes
            public String _firstname;
            public String _middelname;
            public String _lastname;
            public String _callname;
            public String _nameForAddressing;
            public EGenderType _gender;
            public DateTime _birthdate;
            public String _birthplace;
            public String _birthRegisteringAuthority;

            public IRegisterInformation _registerInformation;

            public IAddress _address;
            public IAddress _otherAddress;

            public IContact _contact;
            public IContact _nextOfKinContact;

            public IVirkning _effect;

            public IPersonRelationships _relations;
            public IPersonRelationshipsWithIPerson _relationsWithPerson;

            public ITilstand _tilstand;

            public ITidspunkt _tidspunkt;

            // Builder constructor
            public Builder(int newCode, String newMessage, String newUuid)
            {
                _code = newCode;
                _message = newMessage;
                _uuid = newUuid;
            }

            // build method
            public IPerson build()
            {
                return new Person(this);
            }

            // builder methods
            public Builder firstname(String newName)
            {
                _firstname = newName;
                return this;
            }

            public Builder middelname(String newName)
            {
                _middelname = newName;
                return this;
            }

            public Builder lastname(String newName)
            {
                _lastname = newName;
                return this;
            }

            public Builder callname(String newName)
            {
                _callname = newName;
                return this;
            }

            public Builder nameForAdressing(String newName)
            {
                _nameForAddressing = newName;
                return this;
            }

            public Builder gender(String newGender)
            {
                _gender = (EGenderType)Enum.Parse(typeof(EGenderType), newGender);
                return this;
            }

            public Builder birthdate(DateTime newBirthdate)
            {
                _birthdate = newBirthdate;
                return this;
            }

            public Builder birthplace(String newBirthplace)
            {
                _birthplace = newBirthplace;
                return this;
            }

            public Builder birthRegisteringAuthority(String newBirthRegisteringAuthority)
            {
                _birthRegisteringAuthority = newBirthRegisteringAuthority;
                return this;
            }

            public Builder registerInformation(IRegisterInformation newRegInfo)
            {
                _registerInformation = newRegInfo;
                return this;
            }

            public Builder address(IAddress newAddress)
            {
                _address = newAddress;
                return this;
            }

            public Builder otherAddress(IAddress newAddress)
            {
                _otherAddress = newAddress;
                return this;
            }

            public Builder contact(IContact newContact)
            {
                _contact = newContact;
                return this;
            }

            public Builder nextOfKinContact(IContact newContact)
            {
                _nextOfKinContact = newContact;
                return this;
            }

            public Builder effect(IVirkning newEffect)
            {
                _effect = newEffect;
                return this;
            }

            public Builder relations(IPersonRelationships newRelations)
            {
                _relations = newRelations;
                return this;
            }

            public Builder tilstand(ITilstand newTilstand)
            {
                _tilstand = newTilstand;
                return this;
            }

            public Builder tidspunkt(ITidspunkt newTidspunkt)
            {
                _tidspunkt = newTidspunkt;
                return this;
            }

            public Builder relationsWithPerson(IPersonRelationshipsWithIPerson newRelationsWithPerson)
            {
                _relationsWithPerson = newRelationsWithPerson;
                return this;
            }
        }

        /**
         * Immutable person
         *
         * @param builder
         */
        private Person(Builder builder)
        {
            _code = builder._code;
            _message = builder._message;
            _uuid = builder._uuid;

            _firstname = builder._firstname;
            _middelname = builder._middelname;
            _lastname = builder._lastname;
            _callname = builder._callname;
            _nameForAddressing = builder._nameForAddressing;

            _gender = builder._gender;
            _birthdate = builder._birthdate;
            _birthplace = builder._birthplace;
            _birthRegisteringAuthority = builder._birthRegisteringAuthority;

            _registerInformation = builder._registerInformation;

            _address = builder._address;
            _otherAddress = builder._otherAddress;
            // Now name and address have been set
            _postalLabel = new util.Converters().ToPostalLabel(this);

            _contact = builder._contact;
            _nextOfKinContact = builder._nextOfKinContact;
            _effect = builder._effect;
            _relations = builder._relations;
            _tilstand = builder._tilstand;
            _tidspunkt = builder._tidspunkt;
            _relationsWithPerson = builder._relationsWithPerson;
        }


        // Person attribute accessors


        public String message()
        {
            return _message;
        }


        public int code()
        {
            return _code;
        }


        public String uuid()
        {
            return _uuid;
        }


        public String firstname()
        {
            return _firstname;
        }


        public String middelname()
        {
            return _middelname;
        }


        public String lastname()
        {
            return _lastname;
        }


        public String callname()
        {
            return _callname;
        }


        public String nameForAddressing()
        {
            return _nameForAddressing;
        }


        public EGenderType gender()
        {
            return _gender;
        }


        public DateTime birthdate()
        {
            return _birthdate;
        }


        public String birthplace()
        {
            return _birthplace;
        }


        public String birthRegisteringAuthority()
        {
            return _birthRegisteringAuthority;
        }


        public IRegisterInformation registerInformation()
        {
            return _registerInformation;
        }


        public IAddress address()
        {
            return _address;
        }


        public String[] postalLabel()
        {
            return _postalLabel;
        }


        public IAddress otherAddress()
        {
            return _otherAddress;
        }


        public IContact contact()
        {
            return _contact;
        }


        public IContact nextOfKinContact()
        {
            return _nextOfKinContact;
        }


        public IVirkning effect()
        {
            return _effect;
        }


        public IPersonRelationships relations()
        {
            return _relations;
        }



        public ITilstand tilstand()
        {
            return _tilstand;
        }


        public ITidspunkt tidspunkt()
        {
            return _tidspunkt;
        }


        public IPersonRelationshipsWithIPerson relationsWithPerson()
        {
            return _relationsWithPerson;
        }
    }

}