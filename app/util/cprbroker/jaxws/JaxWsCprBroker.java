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

package util.cprbroker.jaxws;

import dk.magenta.cprbrokersoapfactory.ICPRBrokerSOAPFactory;
import dk.oio.rep.cpr_dk.xml.schemas._2008._05._01.AddressCompleteGreenlandType;
import dk.oio.rep.cpr_dk.xml.schemas._2008._05._01.ForeignAddressStructureType;
import dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13.CountryIdentificationCodeType;
import dk.oio.rep.xkom_dk.xml.schemas._2005._03._15.AddressAccessType;
import dk.oio.rep.xkom_dk.xml.schemas._2006._01._06.AddressPostalType;
import itst.dk.PartSoap12;
import oio.dkal._1_0.ArrayOfString;
import oio.sagdok._2_0.*;
import oio.sagdok.person._1_0.*;
import oio.sagdok.person._1_0.ListOutputType;
import oio.sagdok.person._1_0.RegistreringType;
import oio.sagdok.person._1_0.SoegInputType;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import play.Configuration;
import util.Converters;
import util.cprbroker.*;
import util.cprbroker.models.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JaxWsCprBroker implements ICprBrokerAccessor {

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    private final String endpoint;
    private final boolean usingSsl;
    private final String applicationToken;
    private final String userToken;
    private final int allowedSourceUsageOrderHeader;
    private final boolean fetchRelations;

    private final String keystore;
    private final String keystorePassword;

    private final ICPRBrokerSOAPFactory factory;
    private PartSoap12 localService;
    private PartSoap12 localThenExternalService;
    private PartSoap12 externallService;

    public JaxWsCprBroker(final Configuration config, final ICPRBrokerSOAPFactory newFactory) {

        usingSsl = config.getBoolean("cprbroker.ssl");

        String prefix = (usingSsl) ? HTTPS : HTTP;
        endpoint = prefix + config.getString("cprbroker.endpoint");

        applicationToken = config.getString("cprbroker.applicationtoken");
        userToken = config.getString("cprbroker.usertoken");
        allowedSourceUsageOrderHeader = config.getInt("cprbroker.accesslevel");
        fetchRelations = config.getBoolean("cprbroker.fetchrelations");
        keystore = config.getString("keystorefile");
        keystorePassword = config.getString("keystorepassword");

        play.Logger.debug("JaxWsCprBroker.constructor, endpoint: " + endpoint);
        play.Logger.debug("JaxWsCprBroker.constructor, usingSsl: " + usingSsl);
        play.Logger.debug("JaxWsCprBroker.constructor, appToken: " + applicationToken);
        play.Logger.debug("JaxWsCprBroker.constructor, userToken: " + userToken);
        play.Logger.debug("JaxWsCprBroker.constructor, allowedSourceUsageOrderHeader: " + allowedSourceUsageOrderHeader);
        play.Logger.debug("JaxWsCprBroker.constructor, keystore: " + keystore);
        play.Logger.debug("JaxWsCprBroker.constructor, keystorePassword: " + keystorePassword);

        factory = newFactory;

    }

    /**
     * Helper method to validate an ICPRBrokerSOAPFactory configuration
     *
     * @param config play.Configuration object
     */
    public static void validate(Configuration config) {
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

        String[] stringValues = {"cprbroker.endpoint",
                "cprbroker.applicationtoken",
                "cprbroker.usertoken"};

        // All required String values can't be NULL
        for (String value : stringValues) {
            if (config.getString(value) == null) {
                play.Logger.error("JaxWsCprBroker lacking configuration string: " + value);
                throw new IllegalStateException("JaxWsCprBroker lacking configuration string " + value);
            }
            play.Logger.info(value + " configured with " + config.getString(value));
        }

        if (config.getBoolean("cprbroker.ssl") == null) {
            play.Logger.error("JaxWsCprBroker lacking configuration string: cprbroker.ssl");
            throw new IllegalStateException("JaxWsCprBroker lacking configuration string: cprbroker.ssl");
        } else {
            play.Logger.info("cprbroker.ssl configured with " + config.getString("cprbroker.ssl"));
        }

        Integer accesslevel = config.getInt("cprbroker.accesslevel");
        if (accesslevel == null) {
            play.Logger.error("JaxWsCprBroker lacking configuration string: cprbroker.accesslevel");
            throw new IllegalStateException("JaxWsCprBroker lacking configuration string: cprbroker.accesslevel");
        } else {
            if (accesslevel < 0 || accesslevel > 2) {
                play.Logger.error("cprbroker.accesslevel must be an integer between 0 and 2");
                throw new IllegalStateException("cprbroker.accesslevel must be an integer between 0 and 2");
            }

            play.Logger.info("cprbroker.accesslevel configured with " + accesslevel
                    + " [" + ESourceUsageOrder.values()[accesslevel] + "]");
        }

        if (config.getBoolean("cprbroker.fetchrelations") == null) {
            play.Logger.error("JaxWsCprBroker lacking configuration string: cprbroker.fetchrelations");
            throw new IllegalStateException("JaxWsCprBroker lacking configuration string: cprbroker.fetchrelations");
        } else {
            play.Logger.info("cprbroker.fetchrelations configured with " + config.getString("cprbroker.fetchrelations"));
        }
    }

    /**
     * Helper method for getting a PartSoap12 service
     *
     * @param sourceUsageOrderHeader
     * @return
     * @throws InstantiationException
     */
    private PartSoap12 getService(ESourceUsageOrder sourceUsageOrderHeader) throws InstantiationException {

        // start the performance logging
        StopWatch stopWatch = new Slf4JStopWatch("JaxWsCprBroker.getService");

        if (sourceUsageOrderHeader.ordinal() > allowedSourceUsageOrderHeader) {
            play.Logger.debug("JaxWsCprBroker.getService: sourceUsageOrderHeader value(" +
                    sourceUsageOrderHeader.ordinal() +
                    ") larger than allowed(" +
                    allowedSourceUsageOrderHeader + "). Value set to allowed level.");
            // set the sourceUsageOrderHeader to the maxium allowed level
            sourceUsageOrderHeader = ESourceUsageOrder.values()[allowedSourceUsageOrderHeader];
        }

        // lazy initializing of the needed service
        switch (sourceUsageOrderHeader) {
            case LocalOnly:
                if (localService == null) {
                    localService = createService(sourceUsageOrderHeader);
                }
                stopWatch.stop();
                play.Logger.debug("JaxWsCprBroker.getService: Returned a localService.");
                return localService;

            case LocalThenExternal:
                // check that this level is allowed
                if (localThenExternalService == null) {
                    localThenExternalService = createService(sourceUsageOrderHeader);
                }
                stopWatch.stop();
                play.Logger.debug("JaxWsCprBroker.getService: Returned a localThenExternalService.");
                return localThenExternalService;

            case ExternalOnly:
                // check that this level is allowed
                if (externallService == null) {
                    externallService = createService(sourceUsageOrderHeader);
                }
                stopWatch.stop();
                play.Logger.debug("JaxWsCprBroker.getService: Returned an ExternalService.");
                return externallService;
        }

        // if we ended here an illegal argument was passed ESourceUsageOrder!
        throw new IllegalArgumentException();
    }

    private PartSoap12 createService(final ESourceUsageOrder sourceUsageOrderHeader) throws InstantiationException {

        // start the performance logging
        StopWatch stopWatch = new Slf4JStopWatch("JaxWsCprBroker.createService");

        PartSoap12 tmpService = null;
        factory.setEndpoint(endpoint);
        factory.setUsingSsl(usingSsl);
        factory.setApplicationToken(applicationToken);
        factory.setUserToken(userToken);
        factory.setSourceUsageOrderHeader(sourceUsageOrderHeader.name());
        factory.setKeyStore(keystore);
        factory.setKeyStorePassword(keystorePassword);

        try {
            tmpService = factory.getInstance();
        } catch (KeyManagementException | UnrecoverableKeyException
                | NoSuchAlgorithmException | KeyStoreException
                | CertificateException | IOException e) {
            play.Logger.error(e.getMessage());
        }
        stopWatch.stop();

        return tmpService;

    }

    @Override
    public IUuid getUuid(final String cprNumber) {
        // start the performance logging
        StopWatch stopWatch = new Slf4JStopWatch("JaxWsCprBroker.getUuid");

        PartSoap12 service;
        // start the performance logging
        try {
            service = getService(ESourceUsageOrder.LocalThenExternal);
        } catch (InstantiationException e) {
            play.Logger.error(e.getMessage());
            return null;
        }
        // start performance measurement on cprbroker
        StopWatch stopWatchCprBroker = new Slf4JStopWatch("JaxWsCprBroker.getUuid.CprBroker");
        GetUuidOutputType uuid = service.getUuid(cprNumber);
        // stop measurement on cprbroker
        stopWatchCprBroker.stop();
        // stop the performance logging
        stopWatch.stop();
        return new Uuid(uuid.getUUID(),
                uuid.getStandardRetur().getStatusKode().intValue(),
                uuid.getStandardRetur().getFejlbeskedTekst());
    }


    @Override
    public IUuids search(String firstname, String middlename, String lastname, int maxResults, int startIndex) {
        //start the performance logging
        StopWatch stopWatch = new Slf4JStopWatch("JaxWsCprBroker.search");

        // Setup the input parameters
        SoegInputType input = new SoegInputType();

        // zerobased index of where the search should start
        if (startIndex > 0) {
            input.setFoersteResultatReference(BigInteger.valueOf(startIndex));
        }

        // defaults to 1000 if nothing is specified
        if (maxResults > 0) {
            input.setMaksimalAntalKvantitet(BigInteger.valueOf(maxResults));
        }

        // Set the name search criteria
        Converters converters = new Converters();
        NavnStrukturType navnStrukturType = converters.ToNavnStrukturType(firstname, middlename, lastname);

        SoegEgenskabType soegEgenskabType = new SoegEgenskabType();
        soegEgenskabType.setNavnStruktur(navnStrukturType);

        SoegAttributListeType soegAttributListeType = new SoegAttributListeType();
        List<SoegEgenskabType> soegEgenskabTypeList = soegAttributListeType.getSoegEgenskab();
        soegEgenskabTypeList.add(soegEgenskabType);

        SoegObjektType soegObjekt = new SoegObjektType();
        soegObjekt.setSoegAttributListe(soegAttributListeType);

        input.setSoegObjekt(soegObjekt);


        // Access CPR broker
        PartSoap12 service;
        try {
            service = getService(ESourceUsageOrder.LocalOnly);
        } catch (InstantiationException e) {
            play.Logger.error(e.getMessage());
            return null;
        }
        // start performance measurement on cprbroker
        StopWatch stopWatchCprBroker = new Slf4JStopWatch("JaxWsCprBroker.search.CprBroker");

        SoegOutputType soegOutput = service.search(input);
        // stop performance measurement on cprbroker
        stopWatchCprBroker.stop();
        // Add the Uuids
        ArrayOfString idList = soegOutput.getIdliste();
        List<String> newUuids = null;

        if (idList != null) {
            newUuids = idList.getUUID();
        }

        //return the Uuids
        IUuids uuids = new Uuids(soegOutput.getStandardRetur().getStatusKode().intValue(),
                soegOutput.getStandardRetur().getFejlbeskedTekst(),
                newUuids);

        // stop the performance log
        stopWatch.stop();
        return uuids;
    }

    @Override
    public List<IPerson> searchList(String name, String address, ESourceUsageOrder sourceUsageOrder, int maxResults, int startIndex) {
        //start the performance logging
        StopWatch stopWatch = new Slf4JStopWatch("JaxWsCprBroker.search");

        // Setup the input parameters
        SoegInputType input = new SoegInputType();

        // zerobased index of where the search should start
        if (startIndex > 0) {
            input.setFoersteResultatReference(BigInteger.valueOf(startIndex));
        }

        // defaults to 1000 if nothing is specified
        if (maxResults > 0) {
            input.setMaksimalAntalKvantitet(BigInteger.valueOf(maxResults));
        }

        Converters converters = new Converters();
        SoegObjektType soegObjekt = converters.ToSoegObjektType(name, address);
        input.setSoegObjekt(soegObjekt);

        // Access CPR broker
        PartSoap12 service;
        try {
            service = getService(sourceUsageOrder);
        } catch (InstantiationException e) {
            play.Logger.error(e.getMessage());
            return null;
        }
        // start performance measurement on cprbroker
        StopWatch stopWatchCprBroker = new Slf4JStopWatch("JaxWsCprBroker.search.CprBroker");

        SoegListOutputType soegListOutputType = service.searchList(input);

        // stop performance measurement on cprbroker
        stopWatchCprBroker.stop();

        List<IPerson> persons = null;
        if (soegListOutputType != null
                && soegListOutputType.getStandardRetur() != null
                && soegListOutputType.getStandardRetur().getStatusKode().equals(BigInteger.valueOf(200))) {

            persons = new ArrayList<IPerson>();
            if (soegListOutputType.getLaesResultat() != null) {
                for (int i = 0; i < soegListOutputType.getLaesResultat().size(); i++) {
                    LaesResultatType laesResultatType = soegListOutputType.getLaesResultat().get(i);
                    String uuid = soegListOutputType.getIdliste().getUUID().get(i);


                    persons.add(getPerson(uuid, laesResultatType, soegListOutputType.getStandardRetur(), true));



                }
            }
        }
        // stop the performance log
        stopWatch.stop();
        return persons;
    }


    @Override
    public List<IPerson> list(final IUuids uuids, final ESourceUsageOrder sourceUsageOrder) {

        // start the performance logging
        StopWatch stopWatch = new Slf4JStopWatch("JaxWsCprBroker.list");

        ListInputType listInput = new ListInputType();

        List<String> list = listInput.getUUID();
        list.addAll(uuids.values());


        // Access CPR broker
        PartSoap12 service;
        try {
            service = getService(sourceUsageOrder);
        } catch (InstantiationException e) {
            play.Logger.error(e.getMessage());
            return null;
        }

        // start performance measurement on cprbroker
        StopWatch stopWatchCprBroker = new Slf4JStopWatch("JaxWsCprBroker.list.CprBroker");

        ListOutputType listOutput = service.list(listInput);
        // stop performance measurement on cprbroker
        stopWatchCprBroker.stop();


        List<LaesResultatType> laesResultatTypeList = listOutput.getLaesResultat();

        List<IPerson> persons = new LinkedList<IPerson>();
        IPerson tmpPerson;

        int size = laesResultatTypeList.size();
        List<String> uuidList = uuids.values();

        for (int i = 0; i < size; i++) {
            tmpPerson = getPerson(uuidList.get(i), laesResultatTypeList.get(i), listOutput.getStandardRetur(), false);
            persons.add(tmpPerson);
        }
        // stop performance logging
        stopWatch.stop();

        return persons;

    }

    @Override
    public IPerson read(final String uuid) {
        //start the performance logging
        StopWatch stopWatch = new Slf4JStopWatch("JaxWsCprBroker.read");

        // Setup the input parameters
        LaesInputType laesInput = new LaesInputType();
        laesInput.setUUID(uuid);
        // TODO Make it so you can call read with these parameters
        // laesInput.setRegistreringFraFilter(value) Registrations reported after this date
        // laesInput.setRegistreringTilFilter(value) Registrations reported before this date

        // Access CPR broker
        PartSoap12 service;
        try {
            service = getService(ESourceUsageOrder.LocalThenExternal);
        } catch (InstantiationException e) {
            play.Logger.error(e.getMessage());
            return null;
        }

        // start performance measurement on cprbroker
        StopWatch stopWatchCprBroker = new Slf4JStopWatch("JaxWsCprBroker.read.CprBroker");

        LaesOutputType laesOutput = service.read(laesInput);
        // stop performance measurement on cprbroker
        stopWatchCprBroker.stop();
        // Building a person from the result
        //// Getting the standardReturType
        StandardReturType standardReturType = laesOutput.getStandardRetur();

        IPerson person = getPerson(uuid, laesOutput.getLaesResultat(), standardReturType, this.fetchRelations);

        // stop performance logging
        stopWatch.stop();

        return person;
    }

    private IPerson getPerson(final String uuid, LaesResultatType laesResultatType,
                              StandardReturType standardReturType, boolean isGettingRelations) {

        //start the performance logging
        StopWatch stopWatch = (isGettingRelations) ?
                new Slf4JStopWatch("JaxWsCprBroker.getPerson.withRelations") :
                new Slf4JStopWatch("JaxWsCprBroker.getPerson.withoutRelations");

        //// Start building with the required parameters
        Person.Builder builder =
                new Person.Builder(standardReturType.getStatusKode().intValue(),
                        standardReturType.getFejlbeskedTekst(),
                        uuid);

        //// Did the read return anything?
        //TODO Magic number removal (What status codes can it return?)
        if (standardReturType.getStatusKode().intValue() == 200) {

            // region Registration
            // Only if return is RegistreringType
            if (laesResultatType.getRegistrering() != null) {
                ITidspunkt tidspunkt = getRegisteringsTidspunkt(laesResultatType.getRegistrering());

                builder.tidspunkt(tidspunkt);
            }

            //endregion

            // region Attributes
            AttributListeType attributListeType = getAttributeListType(laesResultatType);

            // Assigning person attributes
            List<EgenskabType> personAttributes = attributListeType.getEgenskab();

            // Get the first from the list
            EgenskabType attributes = personAttributes.get(0);

            // Make certain the person has a name (unborn doesn't!)
            if (attributes != null &&
                    attributes.getNavnStruktur() != null) {

                // Get the givenname
                String firstname = attributes.getNavnStruktur().getPersonNameStructure().getPersonGivenName();
                if (firstname != null) {
                    builder.firstname(firstname);
                }

                // Get the middlename
                String middelName = attributes.getNavnStruktur().getPersonNameStructure().getPersonMiddleName();
                if (middelName != null) {
                    builder.middelname(middelName);
                }

                // Get the surname
                String lastname = attributes.getNavnStruktur().getPersonNameStructure().getPersonSurnameName();
                if (lastname != null) {
                    builder.lastname(lastname);
                }

                // Get the callname
                String callname = attributes.getNavnStruktur().getKaldenavnTekst();
                if (callname != null) {
                    builder.lastname(callname);
                }

                // Get a name for addressing
                String addressingName = attributes.getNavnStruktur().getPersonNameForAddressingName();
                if (addressingName != null) {
                    builder.nameForAdressing(addressingName);
                }

            }
            // Get the gender
            String gender = attributes.getPersonGenderCode().name();
            if (gender != null) {
                builder.gender(gender);
            }

            // Get the birthdate
            XMLGregorianCalendar birthdate = attributes.getBirthDate();
            if (birthdate != null) {
                builder.birthdate(birthdate.toGregorianCalendar());
            }

            // Get the birthplace
            String birthplace = attributes.getFoedestedNavn();
            if (birthplace != null) {
                builder.birthplace(birthplace);
            }

            // Get the birthRegisteringAutherity
            String birthreg = attributes.getFoedselsregistreringMyndighedNavn();
            if (birthreg != null) {
                builder.birthRegisteringAuthority(birthreg);
            }

            // Add the contact information
            IContact newContact = getContact(attributes.getKontaktKanal());
            builder.contact(newContact);

            // Add the next of kin contact information
            IContact newNextOfKinContact = getContact(attributes.getNaermestePaaroerende());
            builder.nextOfKinContact(newNextOfKinContact);

            // Add effect to the person
            IVirkning newEffect = getEffect(attributes.getVirkning());
            builder.effect(newEffect);


            // RegisterOplysning
            IRegisterInformation newRegInfo = getRegisterOplysning(attributListeType);
            builder.registerInformation(newRegInfo);

            // Get the address information and add it to the person
            List<RegisterOplysningType> registerList =
                    attributListeType.getRegisterOplysning();

            // TODO make a guard check if the list has values
            RegisterOplysningType register = registerList.get(0);


            CprBorgerType citizenData = register.getCprBorger();
            IAddress newAddress;
            if (citizenData != null) {
                AdresseType address = citizenData.getFolkeregisterAdresse();
                newAddress = getAddress(address);
                builder.address(newAddress);
            }
            // TODO make a guard check
            AdresseType otherAddress = attributListeType.getEgenskab().get(0).getAndreAdresser();
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
            if (isGettingRelations) {
                // Assigning person relations
                IPersonRelationships newRelations = getAllPersonRelations(laesResultatType);
                if (newRelations != null) {
                    builder.relations(newRelations);

                    List<IRelationship> allRelations = new LinkedList<IRelationship>();

                    // Get all the relations
                    if (newRelations.erstatingAf() != null) allRelations.addAll(newRelations.erstatingAf());
                    if (newRelations.erstatingFor() != null) allRelations.addAll(newRelations.erstatingFor());
                    if (newRelations.fader() != null) allRelations.addAll(newRelations.fader());
                    if (newRelations.moder() != null) allRelations.addAll(newRelations.moder());
                    if (newRelations.foraeldremyndighedsindehaver() != null)
                        allRelations.addAll(newRelations.foraeldremyndighedsindehaver());
                    if (newRelations.retligHandleevneVaergeForPersonen() != null)
                        allRelations.addAll(newRelations.retligHandleevneVaergeForPersonen());
                    if (newRelations.aegtefaelle() != null) allRelations.addAll(newRelations.aegtefaelle());
                    if (newRelations.registreretPartner() != null)
                        allRelations.addAll(newRelations.registreretPartner());
                    if (newRelations.boern() != null) allRelations.addAll(newRelations.boern());
                    if (newRelations.foraeldremydighedsboern() != null)
                        allRelations.addAll(newRelations.foraeldremydighedsboern());
                    if (newRelations.retligHandleevneVaergemaalsindehaver() != null)
                        allRelations.addAll(newRelations.retligHandleevneVaergemaalsindehaver());
                    if (newRelations.bopaelssamling() != null) allRelations.addAll(newRelations.bopaelssamling());

                    List<String> relationUuids = new LinkedList<String>();
                    // Get all the uuids from those relations
                    for (IRelationship relation : allRelations) {
                        relationUuids.add(relation.referenceUuid());
                    }
                    IUuids uuidsFromRelations = new Uuids(200, "", relationUuids);

                    // Get all the persons with those uuids
                    List<IPerson> relationshipPersons = list(uuidsFromRelations, ESourceUsageOrder.LocalOnly);


                    RelationshipWithPerson.Builder relationshipWithPersonBuilder;
                    List<IRelationshipWithIPerson> relationshipsWithPersonList = new LinkedList<IRelationshipWithIPerson>();

                    // Make the IPersonRelationshipsWithIPersons
                    for (int i = 0; i < relationshipPersons.size(); i++) {

                        relationshipWithPersonBuilder = new RelationshipWithPerson.Builder();

                        relationshipWithPersonBuilder.relationship(allRelations.get(i));
                        relationshipWithPersonBuilder.person(relationshipPersons.get(i));

                        relationshipsWithPersonList.add(relationshipWithPersonBuilder.build());
                    }

                    // Add the relationshipsWithPerson to the person
                    builder.relationsWithPerson(new PersonRelationshipsWithPerson(relationshipsWithPersonList));
                }
            }
            //endregion
        }

        // stop performance logging
        stopWatch.stop();

        return builder.build();
    }

    private AttributListeType getAttributeListType(LaesResultatType laesResultatType) {
        if (laesResultatType.getRegistrering() != null)
            return laesResultatType.getRegistrering().getAttributListe();
        else if (laesResultatType.getFiltreretOejebliksbillede() != null)
            return laesResultatType.getFiltreretOejebliksbillede().getAttributListe();
        else
            return null;
    }

    private IRegisterInformation getRegisterOplysning(AttributListeType attributListeType) {

        List<RegisterOplysningType> registerList =
                attributListeType.getRegisterOplysning();

        // TODO make a guard check if the list has values
        RegisterOplysningType register = registerList.get(0);

        //// Make a builder
        CprCitizenData.Builder regInfoBuilder = new CprCitizenData.Builder();

        // Adding virkning to IRegisterInformation
        IVirkning regVirkning = getEffect(register.getVirkning());
        regInfoBuilder.virkning(regVirkning);


        CprBorgerType citizenData = register.getCprBorger();

        if (citizenData != null) {
            // Get social security information
            String socialSecurityNumber = citizenData.getPersonCivilRegistrationIdentifier();
            if (socialSecurityNumber != null) {
                regInfoBuilder.socialSecurityNumber(socialSecurityNumber);
            }

            // Get nationality code
            CountryIdentificationCodeType nationalityCode = citizenData.getPersonNationalityCode();
            if (nationalityCode != null) {
                regInfoBuilder.personNationalityCode(nationalityCode.getValue());
            }

            // Is member of the church?
            Boolean isChurchMember = citizenData.isFolkekirkeMedlemIndikator();
            if (isChurchMember != null) {
                regInfoBuilder.isMemberOfTheChurch(isChurchMember);
            }

            // Has researcher protection?
            Boolean isResearcherProtected = citizenData.isForskerBeskyttelseIndikator();
            if (isResearcherProtected != null) {
                regInfoBuilder.isResearcherProtected(isResearcherProtected);
            }

            // Valid social security number?
            Boolean isCprValid = citizenData.isPersonNummerGyldighedStatusIndikator();
            if (isCprValid != null) {
                regInfoBuilder.isSocialSecurityNumberValid(isCprValid);
            }

            // Name/Address protection?
            Boolean isNameAddressProtected = citizenData.isNavneAdresseBeskyttelseIndikator();
            if (isNameAddressProtected != null) {
                regInfoBuilder.isNameAdressProtected(isNameAddressProtected);
            }

            // Name/Address protection?
            Boolean isPhoneNumberProtected = citizenData.isTelefonNummerBeskyttelseIndikator();
            if (isPhoneNumberProtected != null) {
                regInfoBuilder.isPhoneNumberProtected(isPhoneNumberProtected);
            }
        }
        // Return the register information data to the person
        return regInfoBuilder.build();
    }

    private TilstandListeType getTilstandListeType(LaesResultatType laesResultatType) {
        if (laesResultatType.getRegistrering() != null)
            return laesResultatType.getRegistrering().getTilstandListe();
        else if (laesResultatType.getFiltreretOejebliksbillede() != null)
            return laesResultatType.getFiltreretOejebliksbillede().getTilstandListe();
        else
            return null;
    }

    private ITilstand getTilstande(LaesResultatType laesResultatType) {
        // Assigning PersonTilstand
        TilstandListeType personTilstandsListe =
                getTilstandListeType(laesResultatType);

        // null check
        if (personTilstandsListe != null) {

            // make a builder
            Tilstand.Builder tilstandBuilder = new Tilstand.Builder();

            CivilStatusType civilStatus = personTilstandsListe.getCivilStatus();
            if (civilStatus != null) {
                if (civilStatus.getCivilStatusKode() != null) {
                    tilstandBuilder.civilStatusKode(civilStatus.getCivilStatusKode().name());
                }
                if (civilStatus.getTilstandVirkning() != null) {
                    IVirkning virkning = getTilstandVirkning(civilStatus.getTilstandVirkning());
                    tilstandBuilder.civilTilstandsVirkning(virkning);
                }
            }

            LivStatusType livStatus = personTilstandsListe.getLivStatus();
            if (livStatus != null) {
                if (livStatus.getLivStatusKode() != null) {
                    tilstandBuilder.livStatusKode(livStatus.getLivStatusKode().name());
                }

                if (livStatus.getTilstandVirkning() != null) {
                    IVirkning virkning = getTilstandVirkning(livStatus.getTilstandVirkning());
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
    private IAddress getAddress(AdresseType address) {

        if (address != null) {
            DanskAdresseType danishAddress = address.getDanskAdresse();
            GroenlandAdresseType greenlandicAddress = address.getGroenlandAdresse();
            VerdenAdresseType worldAddress = address.getVerdenAdresse();

            IAddress newAddress = null;

            // Is there a danish address or maybe a Greenlandic or maybe a world?!?
            if (danishAddress != null) {
                newAddress = getDanishAddress(address);
            } else if (greenlandicAddress != null) {
                newAddress = getGreenlandicAddress(address);
            } else if (worldAddress != null) {
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
    private IAddress getWorldAddress(AdresseType address) {

        // get the address
        VerdenAdresseType worldAddress = address.getVerdenAdresse();

        // null guard
        if (worldAddress.getForeignAddressStructure() != null) {

            // Let build a bear.. err world address!
            WorldAddress.Builder addressBuilder = new WorldAddress.Builder();

            // Add any adress notes
            addressBuilder.note(worldAddress.getNoteTekst());

            // reference pointer for less spam
            ForeignAddressStructureType addressPostal = worldAddress.getForeignAddressStructure();

            // Get country id code
            CountryIdentificationCodeType idCode = addressPostal.getCountryIdentificationCode();
            if (idCode != null) {
                addressBuilder.countryIdentificationCode(idCode.getValue());
            }

            // Just build the rest
            addressBuilder.locationDescriptionText(addressPostal.getLocationDescriptionText())
                    .postalAddressFirstLineText(addressPostal.getPostalAddressFirstLineText())
                    .postalAddressSecondLineText(addressPostal.getPostalAddressSecondLineText())
                    .postalAddressThirdLineText(addressPostal.getPostalAddressThirdLineText())
                    .postalAddressFourthLineText(addressPostal.getPostalAddressFourthLineText())
                    .postalAddressFifthLineText(addressPostal.getPostalAddressFifthLineText())
                    .isUkendtAdresseIndikator(worldAddress.isUkendtAdresseIndikator());

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
    private IAddress getGreenlandicAddress(AdresseType address) {

        GroenlandAdresseType greenlandicAddress = address.getGroenlandAdresse();

        // null guard
        if (greenlandicAddress.getAddressCompleteGreenland() != null) {

            // Let build a bear.. err greenlandic address!
            GreenlandicAddress.Builder addressBuilder = new GreenlandicAddress.Builder();

            // Add any adress notes
            addressBuilder.note(greenlandicAddress.getNoteTekst());

            // reference pointer for less spam
            AddressCompleteGreenlandType addressPostal = greenlandicAddress.getAddressCompleteGreenland();

            // Get country id code
            CountryIdentificationCodeType idCode = addressPostal.getCountryIdentificationCode();
            if (idCode != null) {
                addressBuilder.countryIdentificationCode(idCode.getValue());
            }

            // Just build the rest
            addressBuilder.districtName(addressPostal.getDistrictName())
                    .districtSubdivision(addressPostal.getDistrictSubdivisionIdentifier())
                    .floor(addressPostal.getFloorIdentifier())
                    .greenlandBuilding(addressPostal.getGreenlandBuildingIdentifier())
                    .mailDeliverySublocation(addressPostal.getMailDeliverySublocationIdentifier())
                    .municipalityCode(addressPostal.getMunicipalityCode())
                    .postCode(addressPostal.getPostCodeIdentifier())
                    .streetBuilding(addressPostal.getStreetBuildingIdentifier())
                    .streetCode(addressPostal.getStreetCode())
                    .streetName(addressPostal.getStreetName())
                    .streetNameForAddressing(addressPostal.getStreetNameForAddressingName())
                    .suite(addressPostal.getSuiteIdentifier())
                    .isSpecielVejkode(greenlandicAddress.isSpecielVejkodeIndikator())
                    .isUkendtAdresse(greenlandicAddress.isUkendtAdresseIndikator());

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
    private IDanishAddress getDanishAddress(AdresseType address) {

        DanskAdresseType danishAddress = address.getDanskAdresse();
        // null guard
        if (danishAddress.getAddressComplete() != null &&
                danishAddress.getAddressComplete().getAddressPostal() != null) {

            // Let build a bear.. err danish address!
            DanishAddress.Builder addressBuilder = new DanishAddress.Builder();

            // Add any adress notes
            //addressBuilder.note(citizenData.getAdresseNoteTekst());

            // reference pointer for less spam
            AddressAccessType addressAccess = danishAddress.getAddressComplete().getAddressAccess();
            AddressPostalType addressPostal = danishAddress.getAddressComplete().getAddressPostal();

            if (addressPostal != null) {
                // Get country id code
                CountryIdentificationCodeType idCode = addressPostal.getCountryIdentificationCode();
                if (idCode != null) {
                    addressBuilder.countryIdentificationCode(idCode.getValue());
                }

                // Get postofficebox
                BigInteger postOfficeBox = addressPostal.getPostOfficeBoxIdentifier();
                if (postOfficeBox != null) {
                    addressBuilder.postOfficeBox(addressPostal.getPostOfficeBoxIdentifier().toString());
                }

                // Just build the rest
                addressBuilder.districtName(addressPostal.getDistrictName())
                        .districtSubdivision(addressPostal.getDistrictSubdivisionIdentifier())
                        .floor(addressPostal.getFloorIdentifier())
                        .mailSubLocaltion(addressPostal.getMailDeliverySublocationIdentifier())
                        .postCode(addressPostal.getPostCodeIdentifier())
                        .streetBuilding(addressPostal.getStreetBuildingIdentifier())
                        .streetName(addressPostal.getStreetName())
                        .streetNameForAdressing(addressPostal.getStreetNameForAddressingName())
                        .suite(addressPostal.getSuiteIdentifier());
            }

            if (addressAccess != null) {
                addressBuilder.municipalityCode(addressAccess.getMunicipalityCode())
                        .streetBuildingIdentifier(addressAccess.getStreetBuildingIdentifier())
                        .streetCode(addressAccess.getStreetCode());
            }

            addressBuilder.danishNote(danishAddress.getNoteTekst())
                    .politiDistrikt(danishAddress.getPolitiDistriktTekst())
                    .postDistrikt(danishAddress.getPostDistriktTekst())
                    .skoleDistrikt(danishAddress.getSkoleDistriktTekst())
                    .socialDistrikt(danishAddress.getSocialDistriktTekst())
                    .sogneDistrikt(danishAddress.getSogneDistriktTekst())
                    .isSpecielVejkode(danishAddress.isSpecielVejkodeIndikator())
                    .isUkendtAdresse(danishAddress.isUkendtAdresseIndikator());

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
    private ITidspunkt getRegisteringsTidspunkt(RegistreringType registering) {
        if (registering != null) {

            Tidspunkt.Builder tidspunktBuilder = new Tidspunkt.Builder();
            if (registering.getAktoerRef() != null) {

                tidspunktBuilder.aktoerRefUrn(registering.getAktoerRef().getURNIdentifikator())
                        .aktoerRefUuid(registering.getAktoerRef().getUUID());
            }
            if (registering.getTidspunkt() != null) {
                tidspunktBuilder.isTidspunktGraenseIndikator(registering.getTidspunkt().isGraenseIndikator());
                if (registering.getTidspunkt().getTidsstempelDatoTid() != null) {
                    tidspunktBuilder.tidspunkt(registering.getTidspunkt().getTidsstempelDatoTid().toGregorianCalendar());
                }
            }
            if (registering.getLivscyklusKode() != null) {
                tidspunktBuilder.livscyklusKode(registering.getLivscyklusKode().name());
            }
            tidspunktBuilder.kommentar(registering.getCommentText());

            return tidspunktBuilder.build();
        }

        return null;
    }

    private RelationListeType getRelationListeType(LaesResultatType laesResultatType) {
        if (laesResultatType.getRegistrering() != null)
            return laesResultatType.getRegistrering().getRelationListe();
        else if (laesResultatType.getFiltreretOejebliksbillede() != null)
            return laesResultatType.getFiltreretOejebliksbillede().getRelationListe();
        else
            return null;
    }

    private IPersonRelationships getAllPersonRelations(LaesResultatType laesResultatType) {
        RelationListeType personRelations =
                getRelationListeType(laesResultatType);

        // yet another null check
        if (personRelations != null) {
            // Get builder
            PersonRelationships.Builder relationsBuilder = new PersonRelationships.Builder();

            // tmpList for reuse
            List<IRelationship> tmpRelationship;

            // Add PersonRelation
            tmpRelationship = getPersonRelation(personRelations.getAegtefaelle(), ERelationshipType.aegtefaelle);
            relationsBuilder.aegtefaelle(tmpRelationship);

            tmpRelationship = getPersonRelation(personRelations.getErstatningAf(), ERelationshipType.erstatingAf);
            relationsBuilder.erstatingAf(tmpRelationship);

            tmpRelationship = getPersonRelation(personRelations.getFader(), ERelationshipType.fader);
            relationsBuilder.fader(tmpRelationship);

            tmpRelationship = getPersonRelation(personRelations.getForaeldremyndighedsindehaver(), ERelationshipType.foraeldremyndighedsindehaver);
            relationsBuilder.foraeldremyndighedsindehaver(tmpRelationship);

            tmpRelationship = getPersonRelation(personRelations.getModer(), ERelationshipType.moder);
            relationsBuilder.moder(tmpRelationship);

            tmpRelationship = getPersonRelation(personRelations.getRegistreretPartner(), ERelationshipType.registreretPartner);
            relationsBuilder.registreretPartner(tmpRelationship);

            tmpRelationship = getPersonRelation(personRelations.getRetligHandleevneVaergeForPersonen(), ERelationshipType.retligHandleevneVaergeForPersonen);
            relationsBuilder.retligHandleevneVaergeForPersonen(tmpRelationship);

            // Add PersonFlerRelation
            tmpRelationship = getPersonFlerRelation(personRelations.getBoern(), ERelationshipType.boern);
            relationsBuilder.boern(tmpRelationship);

            tmpRelationship = getPersonFlerRelation(personRelations.getBopaelssamling(), ERelationshipType.bopaelssamling);
            relationsBuilder.bopaelssamling(tmpRelationship);

            tmpRelationship = getPersonFlerRelation(personRelations.getErstatningFor(), ERelationshipType.erstatingFor);
            relationsBuilder.erstatingFor(tmpRelationship);

            tmpRelationship = getPersonFlerRelation(personRelations.getForaeldremyndighedsboern(), ERelationshipType.foraeldremydighedsboern);
            relationsBuilder.foraeldremydighedsboern(tmpRelationship);

            tmpRelationship = getPersonFlerRelation(personRelations.getRetligHandleevneVaergemaalsindehaver(), ERelationshipType.retligHandleevneVaergemaalsindehaver);
            relationsBuilder.retligHandleevneVaergemaalsindehaver(tmpRelationship);

            // return the relations to the person
            return relationsBuilder.build();

        }
        return null;
    }

    private List<IRelationship> getPersonFlerRelation(List<PersonFlerRelationType> relations, ERelationshipType type) {

        if (!relations.isEmpty()) {

            // Make a new list
            List<IRelationship> list = new LinkedList<IRelationship>();

            // iterate the relations
            for (PersonFlerRelationType relation : relations) {
                // get builder - NOTICE SINGULAR!
                Relationship.Builder relationBuilder = new Relationship.Builder();

                relationBuilder.type(type);
                relationBuilder.comment(relation.getCommentText());

                if (relation.getReferenceID() != null) {
                    relationBuilder.referenceUrn(relation.getReferenceID().getURNIdentifikator())
                            .referenceUuid(relation.getReferenceID().getUUID());
                }
                // Add effect to the person
                IVirkning newEffect = getEffect(relation.getVirkning());
                relationBuilder.effect(newEffect);

                // add the new relation to the list
                list.add(relationBuilder.build());

            }

            // return the results
            return list;
        }

        return null;
    }

    private List<IRelationship> getPersonRelation(List<PersonRelationType> relations, ERelationshipType type) {

        if (!relations.isEmpty()) {

            // Make a new list
            List<IRelationship> list = new LinkedList<IRelationship>();

            // iterate the relations
            for (PersonRelationType relation : relations) {
                // get builder - NOTICE SINGULAR!
                Relationship.Builder relationBuilder = new Relationship.Builder();
                relationBuilder.type(type);

                relationBuilder.comment(relation.getCommentText());

                if (relation.getReferenceID() != null) {
                    relationBuilder.referenceUrn(relation.getReferenceID().getURNIdentifikator())
                            .referenceUuid(relation.getReferenceID().getUUID());
                }

                // Add effect to the person
                IVirkning newEffect = getEffect(relation.getVirkning());
                relationBuilder.effect(newEffect);

                // add the new relation to the list
                list.add(relationBuilder.build());

            }

            // return the results
            return list;
        }

        return null;
    }

    private IVirkning getTilstandVirkning(TilstandVirkningType virkningType) {

        if (virkningType != null) {
            // Lets build
            Virkning.Builder effectBuilder = new Virkning.Builder();

            UnikIdType actor = virkningType.getAktoerRef();

            if (actor != null) {
                effectBuilder.actorUrn(actor.getURNIdentifikator())
                        .actorUuid(actor.getUUID());
            }

            if (virkningType.getFraTidspunkt() != null) {
                effectBuilder.isEffectiveFromLimit(virkningType.getFraTidspunkt().isGraenseIndikator());

                if (virkningType.getFraTidspunkt().getTidsstempelDatoTid() != null) {
                    effectBuilder.effectiveFromDate(virkningType.getFraTidspunkt().getTidsstempelDatoTid().toGregorianCalendar());
                }
            }

            effectBuilder.comment(virkningType.getCommentText());

            //Return IEffect
            return effectBuilder.build();
        }

        return null;
    }

    private IVirkning getEffect(VirkningType virkningType) {

        if (virkningType != null) {
            // Lets build
            Virkning.Builder effectBuilder = new Virkning.Builder();

            UnikIdType actor = virkningType.getAktoerRef();

            if (actor != null) {
                effectBuilder.actorUrn(actor.getURNIdentifikator())
                        .actorUuid(actor.getUUID());
            }

            if (virkningType.getFraTidspunkt() != null) {
                effectBuilder.isEffectiveFromLimit(virkningType.getFraTidspunkt().isGraenseIndikator());

                if (virkningType.getFraTidspunkt().getTidsstempelDatoTid() != null) {
                    effectBuilder.effectiveFromDate(virkningType.getFraTidspunkt().getTidsstempelDatoTid().toGregorianCalendar());
                }

            }

            if (virkningType.getTilTidspunkt() != null) {
                effectBuilder.isEffectiveToLimit(virkningType.getTilTidspunkt().isGraenseIndikator());

                if (virkningType.getTilTidspunkt().getTidsstempelDatoTid() != null) {
                    effectBuilder.effectiveToDate(virkningType.getTilTidspunkt().getTidsstempelDatoTid().toGregorianCalendar());
                }

            }

            effectBuilder.comment(virkningType.getCommentText());

            //Return IEffect
            return effectBuilder.build();
        }

        return null;
    }

    /**
     * @param contact
     * @return
     */
    private IContact getContact(KontaktKanalType contact) {

        if (contact != null) {
            //Lets build
            Contact.Builder contactBuilder = new Contact.Builder();

            contactBuilder.email(contact.getEmailAddressIdentifier())
                    .limitedUsageText(contact.getBegraensetAnvendelseTekst())
                    .noteText(contact.getNoteTekst());

            if (contact.getAndenKontaktKanal() != null) {
                AndenKontaktKanalType otherContact = contact.getAndenKontaktKanal();

                contactBuilder.otherContactNoteText(otherContact.getNoteTekst())
                        .otherContactText(otherContact.getKontaktKanalTekst());
            }

            if (contact.getTelefon() != null) {
                contactBuilder.phone(contact.getTelefon().getTelephoneNumberIdentifier())
                        .isPhoneAbleToRecieveSms(contact.getTelefon().isKanBrugesTilSmsIndikator());
            }

            return contactBuilder.build();
        }

        return null;
    }
}
