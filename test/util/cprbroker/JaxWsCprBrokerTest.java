package util.cprbroker;

import static org.junit.Assert.*;

import java.math.BigInteger;

import itst.dk.PartSoap12;

import oio.sagdok._2_0.LaesInputType;
import oio.sagdok._2_0.StandardReturType;
import oio.sagdok.person._1_0.GetUuidOutputType;
import oio.sagdok.person._1_0.LaesOutputType;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import dk.magenta.cprbrokersoapfactory.ICPRBrokerSOAPFactory;

import util.cprbroker.jaxws.JaxWsCprBroker;
import util.cprbroker.models.Uuid;

public class JaxWsCprBrokerTest {

	//SUT
	ICprBrokerAccessor testJaxWsCprBroker;
	ICprBrokerAccessor testJaxWsCprBrokerFailure;
	
	//mocks
	ICPRBrokerSOAPFactory mockedFactory;
	PartSoap12 mockedPartSoap12;
	
	
	@Before
	public void setUp() throws InstantiationException {
		
		//Failure factory
		ICPRBrokerSOAPFactory mockedFailureFactory = mock(ICPRBrokerSOAPFactory.class);
		
		when(mockedFailureFactory.getInstance()).thenThrow(new InstantiationException());

		testJaxWsCprBrokerFailure =
				new JaxWsCprBroker("newEndpoint",
									"newApplicationToken",
									"newUserToken",
									mockedFailureFactory);

		// Succesful factory
		ICPRBrokerSOAPFactory mockedFactory = mock(ICPRBrokerSOAPFactory.class);
		mockedPartSoap12 = mock(PartSoap12.class);
		
		when(mockedFactory.getInstance()).thenReturn(mockedPartSoap12);
		
		testJaxWsCprBroker = new JaxWsCprBroker("newEndpoint",
												"newApplicationToken",
												"newUserToken",
												mockedFactory);
	}
	
	@Test
	public void testReadWithBadStandardReturnCode() {
		//Stub a StandartRetur
		StandardReturType standardRetur = mock(StandardReturType.class);
		when(standardRetur.getStatusKode()).thenReturn(new BigInteger("503"));
		when(standardRetur.getFejlbeskedTekst()).thenReturn("OK");

		//Stub a LaesOutputType
		LaesOutputType mockedLaesOutputType = mock(LaesOutputType.class);
		when(mockedLaesOutputType.getStandardRetur()).thenReturn(standardRetur);
		
		
		when(mockedPartSoap12.read(any(LaesInputType.class))).thenReturn(mockedLaesOutputType);
		
		String uuid = "";
		ESourceUsageOrder sourceUsageOrder = ESourceUsageOrder.LocalOnly;
		boolean isGettingRelations = Boolean.TRUE;
		
		IPerson person = testJaxWsCprBroker.read(uuid, sourceUsageOrder, isGettingRelations);
		
		
		//TODO Make an assert!!
	}
	
	@Test
	public void testReadWithFailureFactory() {
		String uuid = "";
		ESourceUsageOrder sourceUsageOrder = ESourceUsageOrder.LocalOnly;
		boolean isGettingRelations = Boolean.TRUE;
		
		IPerson person = testJaxWsCprBrokerFailure.read(uuid, sourceUsageOrder, isGettingRelations);
		assertEquals(null, person);
	}
	
	@Test
	public void testGetUuid() {
		//Stub a standartRetur
		StandardReturType standardRetur = mock(StandardReturType.class);
		when(standardRetur.getStatusKode()).thenReturn(new BigInteger("200"));
		when(standardRetur.getFejlbeskedTekst()).thenReturn("OK");

		//Stub GetUuidOutputType
		GetUuidOutputType mockedGetUuidOutputType = mock(GetUuidOutputType.class);
		when(mockedGetUuidOutputType.getUUID()).thenReturn("12345678-1234-1234-1234-123456789abc");
		when(mockedGetUuidOutputType.getStandardRetur()).thenReturn(standardRetur);
		
		//Mock PartSoap12 to return the mockedGetUuidOutputType
		when(mockedPartSoap12.getUuid("1010701010")).thenReturn(mockedGetUuidOutputType);
		
		//Actual return of the mocking
		IUuid actual = testJaxWsCprBroker.getUuid("1010701010");
		
		//Expected return
		IUuid expected = new Uuid("12345678-1234-1234-1234-123456789abc", 200, "OK");
				
		//Assert that the expected is equal to the actual
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetUuidWithFailureFactory() throws InstantiationException {
		
		IUuid getUuidOutputType = testJaxWsCprBrokerFailure.getUuid("FAILURE");
		
		assertEquals(null, getUuidOutputType);
	}
	
}