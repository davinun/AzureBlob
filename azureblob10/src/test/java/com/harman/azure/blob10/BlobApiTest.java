package com.harman.azure.blob10;

import org.junit.jupiter.api.Test;

import java.io.File;

public class BlobApiTest {

    private BlobConfig config() {
        String accountName = System.getenv("BLOB_ACCOUNT_NAME");
        String accountKey = System.getenv("BLOB_ACCOUNT_KEY");;
        String containerName = System.getenv("BLOB_CONTAINER_NAME");
        return new BlobConfig(accountName, accountKey, containerName);
    }

    @Test
    public void testUploadText() {
        try {
            BlobConfig conf = config();
            BlobApi blobApi = new BlobApi(conf);

            System.out.println("Start uploading text");
            blobApi.uploadText();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testUploadFile() {
        try {
            BlobConfig conf = config();
            BlobApi blobApi = new BlobApi(conf);

            System.out.println("Start uploading file");
            File aFile = new File("c:/DELETE/temp/file.jpg");
            blobApi.uploadFile(aFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testUploadStream() {
        try {
            BlobConfig conf = config();
            BlobApi blobApi = new BlobApi(conf);

            System.out.println("Start uploading stream");
//            File bFile = new File("c:/DELETE/temp/HelloWorld.txt");
            File bFile = new File("c:/DELETE/temp/file.jpg");
//            File bFile = new File("c:/DELETE/temp/pgadmin4-4.2-x86.exe");
            blobApi.uploadStream(bFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
