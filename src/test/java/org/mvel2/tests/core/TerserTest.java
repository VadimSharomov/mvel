package org.mvel2.tests.core;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class TerserTest  {

    Message message;
        Terser terser;
        @Before
        public void setup() throws Exception {
            String m = "MSH|^~\\&|HNAM|D_PROD|Tonic|PORTLAND|20180110011811||SIU^S12|261748|P|2.3\r" +
                    "SCH|||||||^^^Outpatient||||^^^20180226092800\r" +
                    "PID|1|67891^^^TEC MRN|1234567890^^^^TestID|95283^8642|Petrenko^Semen||19910123|Female|x||^^^xxxxx@rocketmail.com||4257891234^^M~^^Internet^Petrenko.Semen@rocketmail.com||SPA|S|CAT|99||||Hispanic||||||Physician\r" +
                    "MRG|9876543210^^^^TestID\r" +
                    "PV1|1|Outpatient Surgery|2052^Serg^^^ClinicSiteID^Ambulatory(s)^Portland|Elective||Outpatient^^^PORTLAND^^^Portland|12345^ProviderLastName^ProviderFirstName^^PA^^^^Enterprise Provider Number^Personnel~270748^Sun^Bing^^PA^^^^Community Doctor No.^Personnel^^^DOCC~103775^Sun^Bing^^PA^^^^Enterprise Provider Number^Personnel~PA174332^Sun^Bing^^PA^^^^^Personnel^^^License Number~MS3189912^Sun^Bing^^PA^^^^DEA No^Personnel^^^Doc Dea~302081^Sun^Bing^^PA^^^^Community Doctor No.^Personnel^^^DOCC~6412967844002^Sun^Bing^^PA^^^^Surescripts Prescriber ID^Pers|xxxxx^xxxxxxx^xxxxx^^MD^^^^^Personnel||ORT||||NonHealth Care |||^^^^^^^^Enterprise Provider Number|Outside Prof Svcs||Medicaid ||||||||||||||||Home/Self Care|||PORTLAND||Discharged|||20180213093000|20180213235900\r" +
                    "RGS|1\r" +
                    "AIL|||2027\r" +
                    "NTE|1||1MONTH HLCK\r" +
                    "AIP|||9876543210^AIP-3-2^AIP-3-3\r";
            //Parse the message
            PipeParser pipeParser = new PipeParser();
            this.message = pipeParser.parse(m);
            this.terser = new Terser(message);
        }

    @Test
    public void testAccessSimpleFields() throws Exception{
        //Patient Id
        assertEquals("4257891234", terser.get("/PID-13-1"));
        //Dob
        assertEquals("M", terser.get("/PID-13-3"));
        //Patient Surname
        assertEquals("Internet", terser.get("/PID-13(1)-3"));
        //Patient First Name
        assertEquals("Petrenko.Semen@rocketmail.com", terser.get("/PID-13(1)-4"));
    }


    @Test
    public void testUpdateSimpleFields() throws Exception{
        //Update Patient Id
        terser.set("/PID-2", "123456");
        assertEquals("123456", terser.get("/PID-2"));
        //Update Dob
        terser.set("/PID-7", "19800101");
        assertEquals("19800101", terser.get("/PID-7"));
    }
    @Test
    public void testSetComponents() throws Exception{
        //Patient Surname
        terser.set("/PID-5-1", "Jones");
        assertEquals("Jones", terser.get("/PID-5-1"));
    }

    @Test
    public void testSetTypeFields() throws Exception{
        //Add a secondary address
        terser.set("/PID-13(1)-4", "Wellington 13-1-4");
        //terser.set("/PID-13(2)-4", "Wellington 13-2-4");
        assertEquals("Wellington 13-1-4", terser.get("/PID-13(1)-4"));
    }
}
