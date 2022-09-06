package pl.psnc.dl.ege.validator.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RNGValidator implements XmlValidator {

    private static final Logger LOGGER = LogManager.getLogger(RNGValidator.class);

    private final String schemeUrl;

    public RNGValidator(String schemeUrl) {
        if (schemeUrl == null) {
            throw new IllegalArgumentException();
        }
        this.schemeUrl = schemeUrl;
    }

    public void validateXml(InputStream inputData, ErrorHandler errorHandler) throws SAXException, FileNotFoundException, IOException, Exception {

        try {
            // Specify you want a factory for RELAX NG
            System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
            LOGGER.debug("Uses xml constant : " + XMLConstants.RELAXNG_NS_URI);
            URL schemaURL = new URL(schemeUrl);
            //try to download schema by external URL
            InputStream urlStream = null;
            try{
                urlStream = schemaURL.openStream();
            }catch(IOException ex){
                    throw ex;
            }
            LOGGER.debug("Uses schema url : " + schemeUrl);
            StreamSource sss = new StreamSource(urlStream);
            LOGGER.debug("Uses schema source : " + sss);
            LOGGER.debug(schemaURL);
            // Load the specific schema you want.
            // Compile the schema.
            Schema schema = factory.newSchema(sss);
            // Get a validator from the schema.
            Validator validator = schema.newValidator();
            validator.setErrorHandler(errorHandler);
            LOGGER.debug("Uses validator : " + validator);
            StreamSource ssi = new StreamSource(inputData);
            validator.validate(ssi);
        } catch (SAXException ex) {
            throw new SAXException(ex);
        }

    }
}
