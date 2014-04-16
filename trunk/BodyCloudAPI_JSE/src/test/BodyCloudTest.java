package test;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;

import weka.core.Instances;

import com.bodycloud.lib.client.BaseClient;

public class BodyCloudTest extends BaseClient{

	public BodyCloudTest(String url) throws ParserConfigurationException {
		super(url);
	}

	@Override
	public Instances getData() {
		return null;
	}

	@Override
	public String handleChoice(String arg0, String[] arg1) {
		return null;
	}

	@Override
	public void handleResourceException(Status arg0, ResourceException arg1) {
	}

	@Override
	public void log(String msg) {
		System.out.println(msg);		
	}

	@Override
	public void log(String msg, Throwable ecc) {
		System.out.println(msg);
		ecc.printStackTrace();		
	}

	@Override
	public void report(Document arg0) {
	}
	
	public static void main(String args[]) {
		try {
			
			//getModalities("http://localhost:8888", "ya29.1.AADtN_Wa94Sfx-JjqXOsniB02vZJIcf1gLpTqyKhIjx3wA8-um3FMzXQPNelOlhUDIxTMm7g5KSfd5m7OAdLCDgZgxFy2S5q4LhqWamduFHy-81csx5ujoKPbozqKwohVg");
			getModalities("https://body-cloud.appspot.com", "ya29.1.AADtN_V0-ddi7kCNbpZx2fsXBqa5AkVilEXJADSxPyXmLubuWM4VwwrNedaIe2ueB1ZsG3Jr24p93sD7XLE0n9fofDDdM7QiQy6Wbjz0-pQGOUCy2FXmfUwTIfR9aR10ew");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
