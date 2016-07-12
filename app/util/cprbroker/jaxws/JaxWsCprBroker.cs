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
using System.Linq;
using cpreader.PartService;
using util.cprbroker.models;
using cpreader.Properties;

namespace util.cprbroker.jaxws
{
    public class JaxWsCprBroker : ICprBrokerAccessor
    {

        public static readonly String HTTP = "http://";
        public static readonly String HTTPS = "https://";

        private String endpoint;
        private bool usingSsl;
        private String applicationToken;
        private String userToken;
        private int allowedSourceUsageOrderHeader;
        private bool fetchRelations;

        private String keystore;
        private String keystorePassword;

        public JaxWsCprBroker()
        {

            usingSsl = cpreader.Properties.Settings.Default.cprbroker_ssl;

            String prefix = (usingSsl) ? HTTPS : HTTP;
            endpoint = prefix + cpreader.Properties.Settings.Default.cprbroker_endpoint;

            applicationToken = cpreader.Properties.Settings.Default.cprbroker_applicationtoken;
            userToken = cpreader.Properties.Settings.Default.cprbroker_usertoken;
            allowedSourceUsageOrderHeader = cpreader.Properties.Settings.Default.cprbroker_accesslevel;
            fetchRelations = cpreader.Properties.Settings.Default.cprbroker_fetchrelations;
            //keystore = config.getString("keystorefile");
            //keystorePassword = config.getString("keystorepassword");

            play.Logger.debug("JaxWsCprBroker.constructor, endpoint: " + endpoint);
            play.Logger.debug("JaxWsCprBroker.constructor, usingSsl: " + usingSsl);
            play.Logger.debug("JaxWsCprBroker.constructor, appToken: " + applicationToken);
            play.Logger.debug("JaxWsCprBroker.constructor, userToken: " + userToken);
            play.Logger.debug("JaxWsCprBroker.constructor, allowedSourceUsageOrderHeader: " + allowedSourceUsageOrderHeader);
            play.Logger.debug("JaxWsCprBroker.constructor, keystore: " + keystore);
            play.Logger.debug("JaxWsCprBroker.constructor, keystorePassword: " + keystorePassword);


        }

        /**
         * Helper method to validate an ICPRBrokerSOAPFactory configuration
         *
         * @param config play.Configuration object
         */
        public static void validate()
        {
            /*
                 # CPR Broker configuration
                # accesslevel can be 0/1/2 which represents localOnly/localThenExternal/externalOnly
                # ~~~~
                cprbroker.ssl = true
                cprbroker.endpoint = "cprbroker.magenta-aps.dk/Services/Part.asmx"
                cprbroker.applicationtoken = "4b8a21cb-3aab-451c-93c8-963142e7db05"
                cprbroker.usertoken = "CPReader"
                cprbroker.accesslevel = 0
                cprbroker.fetchrelations = false
             */

            String[] stringValues = {
                cpreader.Properties.Settings.Default.cprbroker_endpoint,
                cpreader.Properties.Settings.Default.cprbroker_applicationtoken,
                cpreader.Properties.Settings.Default.cprbroker_usertoken};

            // All required String values can't be NULL
            foreach (var value in stringValues)
            {
                if (string.IsNullOrEmpty(value))
                {
                    play.Logger.error("JaxWsCprBroker lacking configuration string: " + value);
                    throw new Exception("JaxWsCprBroker lacking configuration string " + value);
                }
                play.Logger.info(value + " configured with " + value);
            }


            play.Logger.info("cprbroker.ssl configured with " + cpreader.Properties.Settings.Default.cprbroker_ssl);

            int accesslevel = cpreader.Properties.Settings.Default.cprbroker_accesslevel;


            if (accesslevel < 0 || accesslevel > 2)
            {
                play.Logger.error("cprbroker.accesslevel must be an integer between 0 and 2");
                throw new Exception("cprbroker.accesslevel must be an integer between 0 and 2");
            }

            play.Logger.info("cprbroker.accesslevel configured with " + accesslevel
                    + " [" + (ESourceUsageOrder)accesslevel + "]");


            play.Logger.info("cprbroker.fetchrelations configured with " + cpreader.Properties.Settings.Default.cprbroker_fetchrelations);
        }

        /**
         * Helper method for getting a PartSoap12 service
         *
         * @param sourceUsageOrderHeader
         * @return
         * @throws InstantiationException
         */
        private Part getService(ESourceUsageOrder sourceUsageOrder)
        {
            var ret = new cpreader.PartService.Part()
            {
                Url = endpoint,
                ApplicationHeaderValue = new ApplicationHeader() { ApplicationToken = applicationToken, UserToken = userToken },
                SourceUsageOrderHeaderValue = new SourceUsageOrderHeader { SourceUsageOrder = (SourceUsageOrder)(int)sourceUsageOrder },
            };
            if(!string.IsNullOrEmpty(Settings.Default.cpreader_PartService_Part_username))
            {
                ret.Credentials = new System.Net.NetworkCredential(
                    Settings.Default.cpreader_PartService_Part_username, 
                    Settings.Default.cpreader_PartService_Part_password, 
                    Settings.Default.cpreader_PartService_Part_domain);
            }
            else
            {
                ret.UseDefaultCredentials = true;
            }
            return ret;
        }

        public IUuid getUuid(String cprNumber)
        {
            Part service;
            // start the performance logging
            try
            {
                service = getService(ESourceUsageOrder.LocalThenExternal);
            }
            catch (Exception e)
            {
                play.Logger.error(e);
                return null;
            }
            GetUuidOutputType uuid = service.GetUuid(cprNumber);
            return new Uuid(uuid.UUID,
                    int.Parse(uuid.StandardRetur.StatusKode),
                    uuid.StandardRetur.FejlbeskedTekst);
        }
        /*
        public IUuids search(String firstname, String middlename, String lastname, int maxResults, int startIndex)
        {
            // Setup the input parameters
            SoegInputType1 input = new SoegInputType1();

            // zerobased index of where the search should start
            if (startIndex > 0)
            {
                input.FoersteResultatReference = startIndex.ToString();
            }

            // defaults to 1000 if nothing is specified
            if (maxResults > 0)
            {
                input.MaksimalAntalKvantitet = maxResults.ToString();
            }

            // Set the name search criteria
            Converters converters = new Converters();
            NavnStrukturType navnStrukturType = converters.ToNavnStrukturType(firstname, middlename, lastname);

            SoegEgenskabType soegEgenskabType = new SoegEgenskabType();
            soegEgenskabType.NavnStruktur = navnStrukturType;

            SoegAttributListeType soegAttributListeType = new SoegAttributListeType();
            soegAttributListeType.SoegEgenskab = new SoegEgenskabType[] { soegEgenskabType };

            SoegObjektType soegObjekt = new SoegObjektType();
            soegObjekt.SoegAttributListe = soegAttributListeType;

            input.SoegObjekt = soegObjekt;


            // Access CPR broker
            Part service;
            try
            {
                service = getService(ESourceUsageOrder.LocalOnly);
            }
            catch (Exception e)
            {
                play.Logger.error(e);
                return null;
            }

            SoegOutputType soegOutput = service.Search(input);
            // Add the Uuids
            var idList = soegOutput.Idliste;
            List<String> newUuids = null;

            if (idList != null)
            {
                newUuids = idList.UUID;
            }

            //return the Uuids
            IUuids uuids = new Uuids(soegOutput.StandardRetur.StatusKode.intValue(),
                    soegOutput.StandardRetur.FejlbeskedTekst,
                    newUuids);

            return uuids;
        }*/

        public List<IPerson> searchList(String name, String address, ESourceUsageOrder sourceUsageOrder, int maxResults, int startIndex)
        {
            // Setup the input parameters
            SoegInputType1 input = new SoegInputType1();

            // zerobased index of where the search should start
            if (startIndex > 0)
            {
                input.FoersteResultatReference = startIndex.ToString();
            }

            // defaults to 1000 if nothing is specified
            if (maxResults > 0)
            {
                input.MaksimalAntalKvantitet = maxResults.ToString();
            }

            Converters converters = new Converters();
            SoegObjektType soegObjekt = converters.ToSoegObjektType(name, address);
            input.SoegObjekt = soegObjekt;

            // Access CPR broker
            Part service;
            try
            {
                service = getService(sourceUsageOrder);
            }
            catch (Exception e)
            {
                play.Logger.error(e);
                return null;
            }

            SoegListOutputType soegListOutputType = service.SearchList(input);

            List<IPerson> persons = null;
            if (soegListOutputType != null
                    && soegListOutputType.StandardRetur != null
                    && soegListOutputType.StandardRetur.StatusKode.Equals("200"))
            {

                persons = new List<IPerson>();
                if (soegListOutputType.LaesResultat != null)
                {
                    for (int i = 0; i < soegListOutputType.LaesResultat.Length; i++)
                    {
                        LaesResultatType laesResultatType = soegListOutputType.LaesResultat[i];
                        String uuid = soegListOutputType.Idliste[i];

                        persons.Add(getPerson(uuid, laesResultatType, soegListOutputType.StandardRetur, fetchRelations));
                    }
                }
            }
            return persons;
        }


        public List<IPerson> list(IUuids uuids, ESourceUsageOrder sourceUsageOrder)
        {

            ListInputType listInput = new ListInputType() { UUID = uuids.values().ToArray() };


            // Access CPR broker
            Part service;
            try
            {
                service = getService(sourceUsageOrder);
            }
            catch (Exception e)
            {
                play.Logger.error(e.Message);
                return null;
            }

            ListOutputType1 listOutput = service.List(listInput);

            var laesResultatTypeList = listOutput.LaesResultat;

            List<IPerson> persons = new List<IPerson>();
            IPerson tmpPerson;

            int size = laesResultatTypeList.Length;
            List<String> uuidList = uuids.values();

            for (int i = 0; i < size; i++)
            {
                tmpPerson = getPerson(uuidList[i], laesResultatTypeList[i], listOutput.StandardRetur, false);
                persons.Add(tmpPerson);
            }

            return persons;

        }

        public IPerson read(String uuid)
        {
            // Setup the input parameters
            LaesInputType laesInput = new LaesInputType();
            laesInput.UUID = uuid;
            // TODO Make it so you can call read with these parameters
            // laesInput.setRegistreringFraFilter(value) Registrations reported after this date
            // laesInput.setRegistreringTilFilter(value) Registrations reported before this date

            // Access CPR broker
            Part service;
            try
            {
                service = getService(ESourceUsageOrder.LocalThenExternal);
            }
            catch (Exception e)
            {
                play.Logger.error(e.Message);
                return null;
            }

            LaesOutputType laesOutput = service.Read(laesInput);
            // Building a person from the result
            //// Getting the standardReturType
            StandardReturType standardReturType = laesOutput.StandardRetur;

            IPerson person = getPerson(uuid, laesOutput.LaesResultat, standardReturType, this.fetchRelations);

            return person;
        }

        private IPerson getPerson(String uuid, LaesResultatType laesResultatType,
                                  StandardReturType standardReturType, bool isGettingRelations)
        {

            //// Start building with the required parameters
            Person.Builder builder =
                    new Person.Builder(int.Parse(standardReturType.StatusKode),
                            standardReturType.FejlbeskedTekst,
                            uuid);

            //// Did the read return anything?
            //TODO Magic number removal (What status codes can it return?)
            int statusCode = int.Parse(standardReturType.StatusKode);
            bool hasData = laesResultatType != null
                    && laesResultatType.Item != null;
            if (
                    statusCode == 200 ||
                    (statusCode == 206 && hasData)
                    )
            {

                // region Registration
                // Only if return is RegistreringType
                if (laesResultatType.Item is RegistreringType1)
                {
                    ITidspunkt tidspunkt = getRegisteringsTidspunkt(laesResultatType.Item as RegistreringType1);

                    builder.tidspunkt(tidspunkt);
                }

                //endregion

                // region Attributes
                AttributListeType attributListeType = getAttributeListType(laesResultatType);

                // Assigning person attributes
                var personAttributes = attributListeType.Egenskab;

                // Get the first from the list
                EgenskabType attributes = personAttributes[0];

                // Make certain the person has a name (unborn doesn't!)
                if (attributes != null &&
                        attributes.NavnStruktur != null)
                {

                    // Get the givenname
                    String firstname = attributes.NavnStruktur.PersonNameStructure.PersonGivenName;
                    if (firstname != null)
                    {
                        builder.firstname(firstname);
                    }

                    // Get the middlename
                    String middelName = attributes.NavnStruktur.PersonNameStructure.PersonMiddleName;
                    if (middelName != null)
                    {
                        builder.middelname(middelName);
                    }

                    // Get the surname
                    String lastname = attributes.NavnStruktur.PersonNameStructure.PersonSurnameName;
                    if (lastname != null)
                    {
                        builder.lastname(lastname);
                    }

                    // Get the callname
                    String callname = attributes.NavnStruktur.KaldenavnTekst;
                    if (callname != null)
                    {
                        builder.lastname(callname);
                    }

                    // Get a name for addressing
                    String addressingName = attributes.NavnStruktur.PersonNameForAddressingName;
                    if (addressingName != null)
                    {
                        builder.nameForAdressing(addressingName);
                    }

                }
                // Get the gender
                String gender = attributes.PersonGenderCode.ToString();
                if (gender != null)
                {
                    builder.gender(gender);
                }

                // Get the birthdate
                DateTime birthdate = attributes.BirthDate;
                if (birthdate != null)
                {
                    builder.birthdate(birthdate);
                }

                // Get the birthplace
                String birthplace = attributes.FoedestedNavn;
                if (birthplace != null)
                {
                    builder.birthplace(birthplace);
                }

                // Get the birthRegisteringAutherity
                String birthreg = attributes.FoedselsregistreringMyndighedNavn;
                if (birthreg != null)
                {
                    builder.birthRegisteringAuthority(birthreg);
                }

                // Add the contact information
                IContact newContact = getContact(attributes.KontaktKanal);
                builder.contact(newContact);

                // Add the next of kin contact information
                IContact newNextOfKinContact = getContact(attributes.NaermestePaaroerende);
                builder.nextOfKinContact(newNextOfKinContact);

                // Add effect to the person
                IVirkning newEffect = getEffect(attributes.Virkning);
                builder.effect(newEffect);


                // RegisterOplysning
                IRegisterInformation newRegInfo = getRegisterOplysning(attributListeType);
                builder.registerInformation(newRegInfo);

                // Get the address information and add it to the person
                var registerList =
                        attributListeType.RegisterOplysning;

                // TODO make a guard check if the list has values
                RegisterOplysningType register = registerList[0];


                CprBorgerType citizenData = register.Item as CprBorgerType;
                IAddress newAddress;
                if (citizenData != null)
                {
                    AdresseType address = citizenData.FolkeregisterAdresse;
                    newAddress = getAddress(address);
                    builder.address(newAddress);
                }
                // TODO make a guard check
                AdresseType otherAddress = attributListeType.Egenskab[0].AndreAdresser;
                newAddress = getAddress(otherAddress);
                builder.otherAddress(newAddress);

                //endregion

                // region States
                // Assigning person tilstand
                ITilstand newTilstand = getTilstande(laesResultatType);
                builder.tilstand(newTilstand);
                //endregion

                //region Relations
                // Getting the person information for each relation

                if (isGettingRelations)
                {
                    play.Logger.info("getting relations");
                    // Assigning person relations
                    IPersonRelationships newRelations = getAllPersonRelations(laesResultatType);
                    if (newRelations != null)
                    {
                        builder.relations(newRelations);

                        List<IRelationship> allRelations = new List<IRelationship>();

                        // Get all the relations
                        if (newRelations.erstatingAf() != null) allRelations.AddRange(newRelations.erstatingAf());
                        if (newRelations.erstatingFor() != null) allRelations.AddRange(newRelations.erstatingFor());
                        if (newRelations.fader() != null) allRelations.AddRange(newRelations.fader());
                        if (newRelations.moder() != null) allRelations.AddRange(newRelations.moder());
                        if (newRelations.foraeldremyndighedsindehaver() != null)
                            allRelations.AddRange(newRelations.foraeldremyndighedsindehaver());
                        if (newRelations.retligHandleevneVaergeForPersonen() != null)
                            allRelations.AddRange(newRelations.retligHandleevneVaergeForPersonen());
                        if (newRelations.aegtefaelle() != null) allRelations.AddRange(newRelations.aegtefaelle());
                        if (newRelations.registreretPartner() != null)
                            allRelations.AddRange(newRelations.registreretPartner());
                        if (newRelations.boern() != null) allRelations.AddRange(newRelations.boern());
                        if (newRelations.foraeldremydighedsboern() != null)
                            allRelations.AddRange(newRelations.foraeldremydighedsboern());
                        if (newRelations.retligHandleevneVaergemaalsindehaver() != null)
                            allRelations.AddRange(newRelations.retligHandleevneVaergemaalsindehaver());
                        if (newRelations.bopaelssamling() != null) allRelations.AddRange(newRelations.bopaelssamling());

                        List<String> relationUuids = new List<String>();
                        // Get all the uuids from those relations
                        foreach (IRelationship relation in allRelations)
                        {
                            relationUuids.Add(relation.referenceUuid());
                        }
                        IUuids uuidsFromRelations = new Uuids(200, "", relationUuids);

                        // Get all the persons with those uuids
                        List<IPerson> relationshipPersons = list(uuidsFromRelations, ESourceUsageOrder.LocalThenExternal);


                        RelationshipWithPerson.Builder relationshipWithPersonBuilder;
                        List<IRelationshipWithIPerson> relationshipsWithPersonList = new List<IRelationshipWithIPerson>();

                        // Make the IPersonRelationshipsWithIPersons
                        for (int i = 0; i < relationshipPersons.Count; i++)
                        {
                            play.Logger.info("adding relation");
                            relationshipWithPersonBuilder = new RelationshipWithPerson.Builder();

                            relationshipWithPersonBuilder.relationship(allRelations[i]);
                            relationshipWithPersonBuilder.person(relationshipPersons[i]);

                            relationshipsWithPersonList.Add(relationshipWithPersonBuilder.build());
                        }

                        // Add the relationshipsWithPerson to the person
                        builder.relationsWithPerson(new PersonRelationshipsWithPerson(relationshipsWithPersonList));
                    }
                }
                //endregion
            }

            return builder.build();
        }

        private AttributListeType getAttributeListType(LaesResultatType laesResultatType)
        {
            if (laesResultatType.Item is RegistreringType1)
                return (laesResultatType.Item as RegistreringType1).AttributListe;
            else if (laesResultatType.Item is FiltreretOejebliksbilledeType)
                return (laesResultatType.Item as FiltreretOejebliksbilledeType).AttributListe;
            else
                return null;
        }

        private IRegisterInformation getRegisterOplysning(AttributListeType attributListeType)
        {

            var registerList =
                    attributListeType.RegisterOplysning;

            // TODO make a guard check if the list has values
            RegisterOplysningType register = registerList[0];

            //// Make a builder
            CprCitizenData.Builder regInfoBuilder = new CprCitizenData.Builder();

            // Adding virkning to IRegisterInformation
            IVirkning regVirkning = getEffect(register.Virkning);
            regInfoBuilder.virkning(regVirkning);


            CprBorgerType citizenData = register.Item as CprBorgerType;

            if (citizenData != null)
            {
                // Get social security information
                String socialSecurityNumber = citizenData.PersonCivilRegistrationIdentifier;
                if (socialSecurityNumber != null)
                {
                    regInfoBuilder.socialSecurityNumber(socialSecurityNumber);
                }

                // Get nationality code
                CountryIdentificationCodeType nationalityCode = citizenData.PersonNationalityCode;
                if (nationalityCode != null)
                {
                    regInfoBuilder.personNationalityCode(nationalityCode.Value);
                }

                // Is member of the church?
                Boolean isChurchMember = citizenData.FolkekirkeMedlemIndikator;
                //if (isChurchMember != null)
                {
                    regInfoBuilder.isMemberOfTheChurch(isChurchMember);
                }

                // Has researcher protection?
                Boolean isResearcherProtected = citizenData.ForskerBeskyttelseIndikator;
                //if (isResearcherProtected != null)
                {
                    regInfoBuilder.isResearcherProtected(isResearcherProtected);
                }

                // Valid social security number?
                Boolean isCprValid = citizenData.PersonNummerGyldighedStatusIndikator;
                //if (isCprValid != null)
                {
                    regInfoBuilder.isSocialSecurityNumberValid(isCprValid);
                }

                // Name/Address protection?
                Boolean isNameAddressProtected = citizenData.NavneAdresseBeskyttelseIndikator;
                //if (isNameAddressProtected != null)
                {
                    regInfoBuilder.isNameAdressProtected(isNameAddressProtected);
                }

                // Name/Address protection?
                Boolean isPhoneNumberProtected = citizenData.TelefonNummerBeskyttelseIndikator;
                //if (isPhoneNumberProtected != null)
                {
                    regInfoBuilder.isPhoneNumberProtected(isPhoneNumberProtected);
                }
            }
            // Return the register information data to the person
            return regInfoBuilder.build();
        }

        private TilstandListeType getTilstandListeType(LaesResultatType laesResultatType)
        {
            if (laesResultatType.Item is RegistreringType1)
                return (laesResultatType.Item as RegistreringType1).TilstandListe;
            else if (laesResultatType.Item is FiltreretOejebliksbilledeType)
                return (laesResultatType.Item as FiltreretOejebliksbilledeType).TilstandListe;
            else
                return null;
        }

        private ITilstand getTilstande(LaesResultatType laesResultatType)
        {
            // Assigning PersonTilstand
            TilstandListeType personTilstandsListe =
                    getTilstandListeType(laesResultatType);

            // null check
            if (personTilstandsListe != null)
            {

                // make a builder
                Tilstand.Builder tilstandBuilder = new Tilstand.Builder();

                CivilStatusType civilStatus = personTilstandsListe.CivilStatus;
                if (civilStatus != null)
                {
                    //if (civilStatus.CivilStatusKode != null)
                    {
                        tilstandBuilder.civilStatusKode(civilStatus.CivilStatusKode.ToString());
                    }
                    if (civilStatus.TilstandVirkning != null)
                    {
                        IVirkning virkning = getTilstandVirkning(civilStatus.TilstandVirkning);
                        tilstandBuilder.civilTilstandsVirkning(virkning);
                    }
                }

                LivStatusType livStatus = personTilstandsListe.LivStatus;
                if (livStatus != null)
                {
                    //if (livStatus.LivStatusKode != null)
                    {
                        tilstandBuilder.livStatusKode(livStatus.LivStatusKode.ToString());
                    }

                    if (livStatus.TilstandVirkning != null)
                    {
                        IVirkning virkning = getTilstandVirkning(livStatus.TilstandVirkning);
                        tilstandBuilder.livTilstandsVirkning(virkning);
                    }
                }
                //return tilstand to person
                return tilstandBuilder.build();
            }
            return null;
        }

        /**
         * Helper method to figure out what kind of address is attached,
         * call the appropriate helper method to extract that data
         * and return an instance of the the specific IAddress or
         * null if there wasn't any information to extract
         * <p>
         * CAVEAT! CPR Broker doesn't return anything other than
         * DanskAdresseType, so this type is the mostlikely used..
         *
         * @return IDanishAddress, IGreenlandicAddress, IWorldAddress or null
         */
        private IAddress getAddress(AdresseType address)
        {

            if (address != null)
            {
                DanskAdresseType danishAddress = address.Item as DanskAdresseType;
                GroenlandAdresseType greenlandicAddress = address.Item as GroenlandAdresseType;
                VerdenAdresseType worldAddress = address.Item as VerdenAdresseType;

                IAddress newAddress = null;

                // Is there a danish address or maybe a Greenlandic or maybe a world?!?
                if (danishAddress != null)
                {
                    newAddress = getDanishAddress(address);
                }
                else if (greenlandicAddress != null)
                {
                    newAddress = getGreenlandicAddress(address);
                }
                else if (worldAddress != null)
                {
                    newAddress = getWorldAddress(address);
                }

                return newAddress;
            }
            return null;
        }

        /**
         * Helper method for getAddress used to extract a WorldAddressType
         *
         * @return IWorldAddress
         */
        private IAddress getWorldAddress(AdresseType address)
        {

            // get the address
            VerdenAdresseType worldAddress = address.Item as VerdenAdresseType;

            // null guard
            if (worldAddress.ForeignAddressStructure != null)
            {

                // Let build a bear.. err world address!
                WorldAddress.Builder addressBuilder = new WorldAddress.Builder();

                // Add any adress notes
                addressBuilder.note(worldAddress.NoteTekst);

                // reference pointer for less spam
                ForeignAddressStructureType addressPostal = worldAddress.ForeignAddressStructure;

                // Get country id code
                CountryIdentificationCodeType idCode = addressPostal.CountryIdentificationCode;
                if (idCode != null)
                {
                    addressBuilder.countryIdentificationCode(idCode.Value);
                }

                // Just build the rest
                addressBuilder.locationDescriptionText(addressPostal.LocationDescriptionText)
                        .postalAddressFirstLineText(addressPostal.PostalAddressFirstLineText)
                        .postalAddressSecondLineText(addressPostal.PostalAddressSecondLineText)
                        .postalAddressThirdLineText(addressPostal.PostalAddressThirdLineText)
                        .postalAddressFourthLineText(addressPostal.PostalAddressFourthLineText)
                        .postalAddressFifthLineText(addressPostal.PostalAddressFifthLineText)
                        .isUkendtAdresseIndikator(worldAddress.UkendtAdresseIndikator);

                // return the address to the person
                return addressBuilder.build();
            }

            return null;
        }

        /**
         * Helper method for getAddress used to extract a GreenlandicAddressType
         *
         * @return IGreenladicAddress
         */
        private IAddress getGreenlandicAddress(AdresseType address)
        {

            GroenlandAdresseType greenlandicAddress = address.Item as GroenlandAdresseType;

            // null guard
            if (greenlandicAddress.AddressCompleteGreenland != null)
            {

                // Let build a bear.. err greenlandic address!
                GreenlandicAddress.Builder addressBuilder = new GreenlandicAddress.Builder();

                // Add any adress notes
                addressBuilder.note(greenlandicAddress.NoteTekst);

                // reference pointer for less spam
                AddressCompleteGreenlandType addressPostal = greenlandicAddress.AddressCompleteGreenland;

                // Get country id code
                CountryIdentificationCodeType idCode = addressPostal.CountryIdentificationCode;
                if (idCode != null)
                {
                    addressBuilder.countryIdentificationCode(idCode.Value);
                }

                // Just build the rest
                addressBuilder.districtName(addressPostal.DistrictName)
                        .districtSubdivision(addressPostal.DistrictSubdivisionIdentifier)
                        .floor(addressPostal.FloorIdentifier)
                        .greenlandBuilding(addressPostal.GreenlandBuildingIdentifier)
                        .mailDeliverySublocation(addressPostal.MailDeliverySublocationIdentifier)
                        .municipalityCode(addressPostal.MunicipalityCode)
                        .postCode(addressPostal.PostCodeIdentifier)
                        .streetBuilding(addressPostal.StreetBuildingIdentifier)
                        .streetCode(addressPostal.StreetCode)
                        .streetName(addressPostal.StreetName)
                        .streetNameForAddressing(addressPostal.StreetNameForAddressingName)
                        .suite(addressPostal.SuiteIdentifier)
                        .isSpecielVejkode(greenlandicAddress.SpecielVejkodeIndikator)
                        .isUkendtAdresse(greenlandicAddress.UkendtAdresseIndikator);

                // add the address to the person
                return addressBuilder.build();
            }

            return null;

        }

        /**
         * Helper method for getAddress used to extract a DanishAddressType
         *
         * @return IDanishAddress
         */
        private IDanishAddress getDanishAddress(AdresseType address)
        {

            DanskAdresseType danishAddress = address.Item as DanskAdresseType;
            // null guard
            if (danishAddress.AddressComplete != null &&
                    danishAddress.AddressComplete.AddressPostal != null)
            {

                // Let build a bear.. err danish address!
                DanishAddress.Builder addressBuilder = new DanishAddress.Builder();

                // Add any adress notes
                //addressBuilder.note(citizenData.AdresseNoteTekst);

                // reference pointer for less spam
                AddressAccessType addressAccess = danishAddress.AddressComplete.AddressAccess;
                AddressPostalType addressPostal = danishAddress.AddressComplete.AddressPostal;

                if (addressPostal != null)
                {
                    // Get country id code
                    CountryIdentificationCodeType idCode = addressPostal.CountryIdentificationCode;
                    if (idCode != null)
                    {
                        addressBuilder.countryIdentificationCode(idCode.Value);
                    }

                    // Get postofficebox
                    int postOfficeBox = int.Parse(addressPostal.PostOfficeBoxIdentifier);
                    //if (postOfficeBox != null)
                    {
                        addressBuilder.postOfficeBox(addressPostal.PostOfficeBoxIdentifier);
                    }

                    // Just build the rest
                    addressBuilder.districtName(addressPostal.DistrictName)
                            .districtSubdivision(addressPostal.DistrictSubdivisionIdentifier)
                            .floor(addressPostal.FloorIdentifier)
                            .mailSubLocaltion(addressPostal.MailDeliverySublocationIdentifier)
                            .postCode(addressPostal.PostCodeIdentifier)
                            .streetBuilding(addressPostal.StreetBuildingIdentifier)
                            .streetName(addressPostal.StreetName)
                            .streetNameForAdressing(addressPostal.StreetNameForAddressingName)
                            .suite(addressPostal.SuiteIdentifier);
                }

                if (addressAccess != null)
                {
                    addressBuilder.municipalityCode(addressAccess.MunicipalityCode)
                            .streetBuildingIdentifier(addressAccess.StreetBuildingIdentifier)
                            .streetCode(addressAccess.StreetCode);
                }

                addressBuilder.danishNote(danishAddress.NoteTekst)
                        .politiDistrikt(danishAddress.PolitiDistriktTekst)
                        .postDistrikt(danishAddress.PostDistriktTekst)
                        .skoleDistrikt(danishAddress.SkoleDistriktTekst)
                        .socialDistrikt(danishAddress.SocialDistriktTekst)
                        .sogneDistrikt(danishAddress.SogneDistriktTekst)
                        .isSpecielVejkode(danishAddress.SpecielVejkodeIndikator)
                        .isUkendtAdresse(danishAddress.UkendtAdresseIndikator);

                // add the address to the person
                return addressBuilder.build();
            }
            return null;
        }

        /**
         * Helper method to extract TidspunktType
         *
         * @param registering RegistreringType
         * @return An instance of the type ITidspunkt
         */
        private ITidspunkt getRegisteringsTidspunkt(RegistreringType registering)
        {
            if (registering != null)
            {

                Tidspunkt.Builder tidspunktBuilder = new Tidspunkt.Builder();
                if (registering.AktoerRef != null)
                {

                    tidspunktBuilder.aktoerRefUrn(registering.AktoerRef.Item)
                            .aktoerRefUuid(registering.AktoerRef.Item);
                }
                if (registering.Tidspunkt != null)
                {
                    if (registering.Tidspunkt.Item is DateTime)
                    {
                        tidspunktBuilder.tidspunkt((DateTime)registering.Tidspunkt.Item);
                    }
                    else
                    {
                        tidspunktBuilder.isTidspunktGraenseIndikator(bool.Parse(registering.Tidspunkt.Item.ToString()));
                    }
                }
                //if (registering.LivscyklusKode != null)
                {
                    tidspunktBuilder.livscyklusKode(registering.LivscyklusKode.ToString());
                }
                tidspunktBuilder.kommentar(registering.CommentText);

                return tidspunktBuilder.build();
            }

            return null;
        }

        private RelationListeType getRelationListeType(LaesResultatType laesResultatType)
        {
            if (laesResultatType.Item is RegistreringType1)
                return (laesResultatType.Item as RegistreringType1).RelationListe;
            else if (laesResultatType.Item is FiltreretOejebliksbilledeType)
                return (laesResultatType.Item as FiltreretOejebliksbilledeType).RelationListe;
            else
                return null;
        }

        private IPersonRelationships getAllPersonRelations(LaesResultatType laesResultatType)
        {
            RelationListeType personRelations =
                    getRelationListeType(laesResultatType);

            // yet another null check
            if (personRelations != null)
            {
                // Get builder
                PersonRelationships.Builder relationsBuilder = new PersonRelationships.Builder();

                // tmpList for reuse
                List<IRelationship> tmpRelationship;

                // Add PersonRelation
                tmpRelationship = getPersonRelation(personRelations.Aegtefaelle.ToList(), ERelationshipType.aegtefaelle);
                relationsBuilder.aegtefaelle(tmpRelationship);

                tmpRelationship = getPersonRelation(personRelations.ErstatningAf.ToList(), ERelationshipType.erstatingAf);
                relationsBuilder.erstatingAf(tmpRelationship);

                tmpRelationship = getPersonRelation(personRelations.Fader.ToList(), ERelationshipType.fader);
                relationsBuilder.fader(tmpRelationship);

                tmpRelationship = getPersonRelation(personRelations.Foraeldremyndighedsindehaver.ToList(), ERelationshipType.foraeldremyndighedsindehaver);
                relationsBuilder.foraeldremyndighedsindehaver(tmpRelationship);

                tmpRelationship = getPersonRelation(personRelations.Moder.ToList(), ERelationshipType.moder);
                relationsBuilder.moder(tmpRelationship);

                tmpRelationship = getPersonRelation(personRelations.RegistreretPartner.ToList(), ERelationshipType.registreretPartner);
                relationsBuilder.registreretPartner(tmpRelationship);

                tmpRelationship = getPersonRelation(personRelations.RetligHandleevneVaergeForPersonen.ToList(), ERelationshipType.retligHandleevneVaergeForPersonen);
                relationsBuilder.retligHandleevneVaergeForPersonen(tmpRelationship);

                // Add PersonFlerRelation
                tmpRelationship = getPersonFlerRelation(personRelations.Boern.ToList(), ERelationshipType.boern);
                relationsBuilder.boern(tmpRelationship);

                tmpRelationship = getPersonFlerRelation(personRelations.Bopaelssamling.ToList(), ERelationshipType.bopaelssamling);
                relationsBuilder.bopaelssamling(tmpRelationship);

                tmpRelationship = getPersonFlerRelation(personRelations.ErstatningFor.ToList(), ERelationshipType.erstatingFor);
                relationsBuilder.erstatingFor(tmpRelationship);

                tmpRelationship = getPersonFlerRelation(personRelations.Foraeldremyndighedsboern.ToList(), ERelationshipType.foraeldremydighedsboern);
                relationsBuilder.foraeldremydighedsboern(tmpRelationship);

                tmpRelationship = getPersonFlerRelation(personRelations.RetligHandleevneVaergemaalsindehaver.ToList(), ERelationshipType.retligHandleevneVaergemaalsindehaver);
                relationsBuilder.retligHandleevneVaergemaalsindehaver(tmpRelationship);

                // return the relations to the person
                return relationsBuilder.build();

            }
            return null;
        }

        private List<IRelationship> getPersonFlerRelation(List<PersonFlerRelationType> relations, ERelationshipType type)
        {

            if (relations.FirstOrDefault() != null)
            {

                // Make a new list
                List<IRelationship> list = new List<IRelationship>();

                // iterate the relations
                foreach (PersonFlerRelationType relation in relations)
                {
                    // get builder - NOTICE SINGULAR!
                    Relationship.Builder relationBuilder = new Relationship.Builder();

                    relationBuilder.type(type);
                    relationBuilder.comment(relation.CommentText);

                    if (relation.ReferenceID != null)
                    {
                        relationBuilder.referenceUrn(relation.ReferenceID.Item)
                                .referenceUuid(relation.ReferenceID.Item);
                    }
                    // Add effect to the person
                    IVirkning newEffect = getEffect(relation.Virkning);
                    relationBuilder.effect(newEffect);

                    // add the new relation to the list
                    list.Add(relationBuilder.build());

                }

                // return the results
                return list;
            }

            return null;
        }

        private List<IRelationship> getPersonRelation(List<PersonRelationType> relations, ERelationshipType type)
        {

            if (relations.FirstOrDefault() != null)
            {

                // Make a new list
                List<IRelationship> list = new List<IRelationship>();

                // iterate the relations
                foreach (PersonRelationType relation in relations)
                {
                    // get builder - NOTICE SINGULAR!
                    Relationship.Builder relationBuilder = new Relationship.Builder();
                    relationBuilder.type(type);

                    relationBuilder.comment(relation.CommentText);

                    if (relation.ReferenceID != null)
                    {
                        // TODO : Double setting of Item to UrnIdentificator and Uuid
                        relationBuilder.referenceUrn(relation.ReferenceID.Item)
                                .referenceUuid(relation.ReferenceID.Item);
                    }

                    // Add effect to the person
                    IVirkning newEffect = getEffect(relation.Virkning);
                    relationBuilder.effect(newEffect);

                    // add the new relation to the list
                    list.Add(relationBuilder.build());

                }

                // return the results
                return list;
            }

            return null;
        }

        private IVirkning getTilstandVirkning(TilstandVirkningType virkningType)
        {

            if (virkningType != null)
            {
                // Lets build
                Virkning.Builder effectBuilder = new Virkning.Builder();

                UnikIdType actor = virkningType.AktoerRef;

                if (actor != null)
                {
                    effectBuilder.actorUrn(actor.Item)
                            .actorUuid(actor.Item);
                }

                if (virkningType.FraTidspunkt != null)
                {

                    if (virkningType.FraTidspunkt.Item is DateTime)
                    {
                        effectBuilder.effectiveFromDate((DateTime)virkningType.FraTidspunkt.Item);
                    }
                    else
                    {
                        effectBuilder.isEffectiveFromLimit((bool)virkningType.FraTidspunkt.Item);

                    }
                }

                effectBuilder.comment(virkningType.CommentText);

                //Return IEffect
                return effectBuilder.build();
            }

            return null;
        }

        private IVirkning getEffect(VirkningType virkningType)
        {

            if (virkningType != null)
            {
                // Lets build
                Virkning.Builder effectBuilder = new Virkning.Builder();

                UnikIdType actor = virkningType.AktoerRef;

                if (actor != null)
                {
                    effectBuilder.actorUrn(actor.Item)
                            .actorUuid(actor.Item);
                }

                if (virkningType.FraTidspunkt != null)
                {
                    if (virkningType.FraTidspunkt.Item is DateTime)
                    {
                        effectBuilder.effectiveFromDate((DateTime)virkningType.FraTidspunkt.Item);
                    }
                    else
                    {
                        effectBuilder.isEffectiveFromLimit((bool)virkningType.FraTidspunkt.Item);
                    }

                }

                if (virkningType.TilTidspunkt != null)
                {
                    if (virkningType.TilTidspunkt.Item is DateTime)
                    {
                        effectBuilder.effectiveToDate((DateTime)virkningType.TilTidspunkt.Item);
                    }
                    else
                    {
                        effectBuilder.isEffectiveToLimit((bool)virkningType.TilTidspunkt.Item);
                    }

                }

                effectBuilder.comment(virkningType.CommentText);

                //Return IEffect
                return effectBuilder.build();
            }

            return null;
        }

        /**
         * @param contact
         * @return
         */
        private IContact getContact(KontaktKanalType contact)
        {

            if (contact != null)
            {
                //Lets build
                Contact.Builder contactBuilder = new Contact.Builder();

                if (contact.Item is string)
                {
                    contactBuilder.email(contact.Item as string)
                            .limitedUsageText(contact.BegraensetAnvendelseTekst)
                            .noteText(contact.NoteTekst);
                }

                if (contact.Item is AndenKontaktKanalType)
                {
                    AndenKontaktKanalType otherContact = contact.Item as AndenKontaktKanalType;

                    contactBuilder.otherContactNoteText(otherContact.NoteTekst)
                            .otherContactText(otherContact.KontaktKanalTekst);
                }

                if (contact.Item is TelefonType)
                {
                    contactBuilder.phone((contact.Item as TelefonType).TelephoneNumberIdentifier)
                            .isPhoneAbleToRecieveSms((contact.Item as TelefonType).KanBrugesTilSmsIndikator);
                }

                return contactBuilder.build();
            }

            return null;
        }
    }
}