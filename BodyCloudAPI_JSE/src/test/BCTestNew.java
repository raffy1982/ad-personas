package test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import com.bodycloud.lib.client.BaseClient;
import com.bodycloud.lib.domain.ModalitySpecification;

public class BCTestNew extends BaseClient {
	
	

	public BCTestNew(String url) throws ParserConfigurationException {
		super(url);
	}

	public BCTestNew(String url, ModalitySpecification modality)
			throws ParserConfigurationException {
		super(url, modality);
	}

	@Override
	public void log(String message, Throwable thrown) {
		System.out.println(message);
		thrown.printStackTrace();
	}
	
	public static double[] readData(String file) {
		BufferedReader in = null;
		Vector<Double> sign = new Vector<Double>();
		try {
			InputStream stream = BCTestNew.class.getClassLoader().getResourceAsStream(file);
			in = new BufferedReader(new InputStreamReader(stream));
			String line = in.readLine(); //header
			line = in.readLine();
			while (line != null) {
				sign.add(Double.parseDouble(line));
				line = in.readLine();
			}
			;
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		double[] res = new double[sign.size()];
		for (int i = 0; i < sign.size(); i++) {
			res[i] = sign.get(i);
		}
		return res;
	}

	
	@Override
	public String handleChoice(String parameterName, String[] choices) {
		return choices[0];
	}

	@Override
	public void report(Document view) {
		System.out.println("this is a report");
	}
	
	@Override
	public Instances getData()
	{
		System.out.println("in getdata");
		ArrayList<Attribute> info = new ArrayList<Attribute>();
		Instances instances = new Instances("ECGstreams", info, 500);
		
		for (int i = 0; i < 500; i++)
		{
			Instance instance = new DenseInstance(1, new double[]{ i });
			instances.add(instance);
		}
		return instances;
	}
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8888";
		//String url = "https://snapshot.kd-cloud.appspot.com";
		
		
		
		
		
		
		BaseClient kdcloud = new BCTestNew(url);
		kdcloud.setRepeatAllowed(false);
		kdcloud.setAuthentication("luigisalvatoregalluzzi@gmail.com", "ya29.1.AADtN_UeecMojLGBcT3pJSqbq1PWP2ldOqphw3F4mfqwq7lsgEyZEHv9qq4AW3U6ACuWcZt74KFHDfpAkGvMr7YezLg7FrmYy0rXR8p5Nc8Ct6CK3Uq5N-vdPa4l");
		List<ModalitySpecification> modalities = kdcloud.getModalities();
		ModalitySpecification dataFeed = modalities.get(0);

		kdcloud.setModality(dataFeed);
		
		kdcloud.executeModality();
		
		//ModalitySpecification single = modalities.get(1);
		//kdcloud.setModality(single);
		//kdcloud.executeModality();
		//ModalitySpecification global = modalities.get(2);
		//kdcloud.setModality(global);
		//kdcloud.executeModality();
	}

	@Override
	public void log(String message) {
		System.out.println(message);
	}

	@Override
	public void handleResourceException(Status status, ResourceException e) {
		System.out.println(status);
	}

}
