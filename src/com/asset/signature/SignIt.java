package com.asset.signature;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

public class SignIt extends CreateSignatureBase{
    
    public File outFile = null;
    public boolean isSigned = false;

    public SignIt(File file) {
        try {
            File inFile = file;
            outFile = new File("signed.pdf");
            signDetached(inFile, outFile);
            this.isSigned = true;
            
        } catch (IOException ex) {
            Logger.getLogger(SignIt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void signDetached(File inFile, File outFile) throws IOException {
        if (inFile == null || !inFile.exists()) {
            throw new FileNotFoundException("El documento a firmar no existe");
        }

        FileOutputStream fos = new FileOutputStream(outFile);

        PDDocument doc = PDDocument.load(inFile);
        signDetached(doc, fos);
        doc.close();
    }

    public void signDetached(PDDocument document, OutputStream output) throws IOException {
        
        int accessPermissions = getMDPPermission(document);
        System.out.println(accessPermissions);
        if (accessPermissions == 1) {
            throw new IllegalStateException("No se permiten cambios en el documento debido al diccionario de parámetros de transformación DocMDP");
        }     

        // create signature dictionary
        PDSignature signature = new PDSignature();
        
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED); 
        signature.setName("Usuario de OFC");
        signature.setLocation("Organización Firmante Cool");
        signature.setReason("Protección que es cool y se hace notar");

        // the signing date, needed for valid signature
        signature.setSignDate(Calendar.getInstance());

        //  certify 
        if (accessPermissions == 0)
        {
            setMDPPermission(document, signature, 2);
        }        

        
        System.out.println("Firmar de manera externa");
        document.addSignature(signature);
        ExternalSigningSupport externalSigning = document.saveIncrementalForExternalSigning(output);
        byte[] cmsSignature = sign(externalSigning.getContent());
        externalSigning.setSignature(cmsSignature);
        
        
    }
}
