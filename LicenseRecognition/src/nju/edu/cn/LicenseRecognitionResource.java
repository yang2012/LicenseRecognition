package nju.edu.cn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.data.Disposition;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Created by justinyang on 13-12-18.
 */
public class LicenseRecognitionResource extends ServerResource {
	@Get
	public String sayHello() {
		return "Hello world";
	}
	
	@Post
	public String upload(Representation input) throws ResourceException {
		StringBuffer licenseBuffer = new StringBuffer();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1000240);

		RestletFileUpload upload = new RestletFileUpload(factory);
		List<FileItem> items = null;
		try {
			items = upload.parseRepresentation(input);

			try {
				String license = null;
				for (final Iterator<FileItem> it = items.iterator(); it
						.hasNext();) {
					FileItem fi = it.next();

					if (fi.isFormField()) {

					} else {
						String fileName = fi.getName();
                        if (fileName.isEmpty()) {
                            continue;
                        }

                        File container = new File(Constants.kUploadImageContainerDirectory);
                        if (!container.exists()) {
                        	container.mkdirs();
                        }
						String fileDirecotry = Constants.kUploadImageContainerDirectory + fileName;
						fi.getInputStream();
						fi.write(new File(fileDirecotry));
						license = this.recognize(fileDirecotry);
						
						if (license != null) {
							licenseBuffer.append(license);
						}
					}
				}
				getResponse().setStatus(Status.SUCCESS_CREATED);
			} catch (Exception e) {
                e.printStackTrace();
				throw e;
			}
		} catch (Exception e) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			e.printStackTrace();
		}
		return licenseBuffer.toString();
	}

    private String recognize(String fileDirectory) {
    	String command = Constants.kOpenalprDirectory + "src/alpr " + fileDirectory + " -r " + Constants.kOpenalprDirectory + "runtime_data/";
    	return this.executeCommand(command);
    }
    
    private String executeCommand(String command) {  	 
		StringBuffer output = new StringBuffer();
 
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line = "";
			
			String newLineSeparator = System.getProperty("line.separator");
			while ((line = inputReader.readLine())!= null) {
				output.append(line + newLineSeparator);
			}
			
			line = "";
			while ((line = errorReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return output.toString();
	}
}
