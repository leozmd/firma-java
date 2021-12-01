package digitalsignature;

import com.asset.signature.SignIt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DigitalSignature {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        File file = new File("PananiAPIDocumentation.pdf");
        
        SignIt sign = new SignIt(file);
    }
    
}
