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

package util.cprbroker.models;

import java.util.GregorianCalendar;

import org.joda.time.DateTime;

import util.cprbroker.EGenderType;
import util.cprbroker.IAddress;
import util.cprbroker.IContact;
import util.cprbroker.IPerson;
import util.cprbroker.IPersonRelationships;
import util.cprbroker.IPersonRelationshipsWithIPerson;
import util.cprbroker.IRegisterInformation;
import util.cprbroker.ITidspunkt;
import util.cprbroker.ITilstand;
import util.cprbroker.IVirkning;

public class Person implements IPerson {

    // basic attributes
    private final int code;
    private final String message;
    private final String uuid;

    private final String firstname;
    private final String middelname;
    private final String lastname;
    private final String callname;
    private final String nameForAddressing;
    private final EGenderType gender;
    private final DateTime birthdate;
    private final String birthplace;
    private final String birthRegisteringAuthority;

    // register information
    private final IRegisterInformation registerInformation;

    // address
    private final IAddress address;
    private final String[] postalLabel;
    private final IAddress otherAddress;

    // contact
    private final IContact contact;
    private final IContact nextOfKinContact;

    // effect
    private final IVirkning effect;

    // relationships
    private final IPersonRelationships relations;
    private final IPersonRelationshipsWithIPerson relationsWithPerson;

    // tilstand
    private final ITilstand tilstand;

    private final ITidspunkt tidspunkt;

    /**
     * Builder for a Person
     *
     * @author Søren Kirkegård
     */
    public static class Builder {
        //Required parameters
        private final int code;
        private final String message;
        private final String uuid;

        //Optional parameters - initialized to default values
        ////IPersonAttributes
        private String firstname;
        private String middelname = null;
        private String lastname = null;
        private String callname = null;
        private String nameForAddressing = null;
        private EGenderType gender = null;
        private DateTime birthdate = null;
        private String birthplace = null;
        private String birthRegisteringAuthority = null;

        private IRegisterInformation registerInformation = null;

        private IAddress address = null;
        private IAddress otherAddress = null;

        private IContact contact = null;
        private IContact nextOfKinContact = null;

        private IVirkning effect = null;

        private IPersonRelationships relations = null;
        private IPersonRelationshipsWithIPerson relationsWithPerson = null;

        private ITilstand tilstand = null;

        private ITidspunkt tidspunkt = null;

        // Builder constructor
        public Builder(final int newCode, final String newMessage, final String newUuid) {
            code = newCode;
            message = newMessage;
            uuid = newUuid;
        }

        // build method
        public IPerson build() {
            return new Person(this);
        }

        // builder methods
        public Builder firstname(final String newName) {
            firstname = newName;
            return this;
        }

        public Builder middelname(final String newName) {
            middelname = newName;
            return this;
        }

        public Builder lastname(final String newName) {
            lastname = newName;
            return this;
        }

        public Builder callname(final String newName) {
            callname = newName;
            return this;
        }

        public Builder nameForAdressing(final String newName) {
            nameForAddressing = newName;
            return this;
        }

        public Builder gender(final String newGender) {
            gender = EGenderType.valueOf(newGender);
            return this;
        }

        public Builder birthdate(final GregorianCalendar newBirthdate) {
            birthdate = new DateTime(newBirthdate);
            return this;
        }

        public Builder birthplace(final String newBirthplace) {
            birthplace = newBirthplace;
            return this;
        }

        public Builder birthRegisteringAuthority(final String newBirthRegisteringAuthority) {
            birthRegisteringAuthority = newBirthRegisteringAuthority;
            return this;
        }

        public Builder registerInformation(final IRegisterInformation newRegInfo) {
            registerInformation = newRegInfo;
            return this;
        }

        public Builder address(final IAddress newAddress) {
            address = newAddress;
            return this;
        }

        public Builder otherAddress(final IAddress newAddress) {
            otherAddress = newAddress;
            return this;
        }

        public Builder contact(final IContact newContact) {
            contact = newContact;
            return this;
        }

        public Builder nextOfKinContact(final IContact newContact) {
            nextOfKinContact = newContact;
            return this;
        }

        public Builder effect(final IVirkning newEffect) {
            effect = newEffect;
            return this;
        }

        public Builder relations(final IPersonRelationships newRelations) {
            relations = newRelations;
            return this;
        }

        public Builder tilstand(final ITilstand newTilstand) {
            tilstand = newTilstand;
            return this;
        }

        public Builder tidspunkt(final ITidspunkt newTidspunkt) {
            tidspunkt = newTidspunkt;
            return this;
        }

        public Builder relationsWithPerson(final IPersonRelationshipsWithIPerson newRelationsWithPerson) {
            relationsWithPerson = newRelationsWithPerson;
            return this;
        }
    }

    /**
     * Immutable person
     *
     * @param builder
     */
    private Person(Builder builder) {
        code = builder.code;
        message = builder.message;
        uuid = builder.uuid;

        firstname = builder.firstname;
        middelname = builder.middelname;
        lastname = builder.lastname;
        callname = builder.callname;
        nameForAddressing = builder.nameForAddressing;

        gender = builder.gender;
        birthdate = builder.birthdate;
        birthplace = builder.birthplace;
        birthRegisteringAuthority = builder.birthRegisteringAuthority;

        registerInformation = builder.registerInformation;

        address = builder.address;
        otherAddress = builder.otherAddress;
        // Now name and address have been set
        postalLabel = new util.Converters().ToPostalLabel(this);

        contact = builder.contact;
        nextOfKinContact = builder.nextOfKinContact;
        effect = builder.effect;
        relations = builder.relations;
        tilstand = builder.tilstand;
        tidspunkt = builder.tidspunkt;
        relationsWithPerson = builder.relationsWithPerson;
    }


    // Person attribute accessors

    @Override
    public String message() {
        return message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String uuid() {
        return uuid;
    }

    @Override
    public String firstname() {
        return firstname;
    }

    @Override
    public String middelname() {
        return middelname;
    }

    @Override
    public String lastname() {
        return lastname;
    }

    @Override
    public String callname() {
        return callname;
    }

    @Override
    public String nameForAddressing() {
        return nameForAddressing;
    }

    @Override
    public EGenderType gender() {
        return gender;
    }

    @Override
    public DateTime birthdate() {
        return birthdate;
    }

    @Override
    public String birthplace() {
        return birthplace;
    }

    @Override
    public String birthRegisteringAuthority() {
        return birthRegisteringAuthority;
    }

    @Override
    public IRegisterInformation registerInformation() {
        return registerInformation;
    }

    @Override
    public IAddress address() {
        return address;
    }

    @Override
    public String[] postalLabel() {
        return postalLabel;
    }

    @Override
    public IAddress otherAddress() {
        return otherAddress;
    }

    @Override
    public IContact contact() {
        return contact;
    }

    @Override
    public IContact nextOfKinContact() {
        return nextOfKinContact;
    }

    @Override
    public IVirkning effect() {
        return effect;
    }

    @Override
    public IPersonRelationships relations() {
        return relations;
    }


    @Override
    public ITilstand tilstand() {
        return tilstand;
    }

    @Override
    public ITidspunkt tidspunkt() {
        return tidspunkt;
    }

    @Override
    public IPersonRelationshipsWithIPerson relationsWithPerson() {
        return relationsWithPerson;
    }
}
