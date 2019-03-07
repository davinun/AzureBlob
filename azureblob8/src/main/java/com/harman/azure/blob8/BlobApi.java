package com.harman.azure.blob8;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;

public class BlobApi {

    private static String getAccountName() {
        return System.getenv("BLOB_ACCOUNT_NAME");
    }

    private static String getAccountKey() {
        return System.getenv("BLOB_ACCOUNT_KEY");
    }

    private static String getContainerName() {
        return System.getenv("BLOB_CONTAINER_NAME");
    }

    public static void main(String[] args) {
        try {

            String accountName = getAccountName();
            String accountKey = getAccountKey();
            String containerName = getContainerName();
//            File inFile = new File("c:/DELETE/temp/file.jpg");
            File inFile = new File("c:/users/davinun/Downloads/pgadmin4-4.2-x86.exe");

            String storageConnectionString =
                    "DefaultEndpointsProtocol=https;"
                            + "AccountName="+accountName+";"
                            + "AccountKey="+accountKey;

            // Setup the cloud storage account.
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);

            // Create a blob service client
            CloudBlobClient blobClient = account.createCloudBlobClient();

            // Get a reference to a container
            // The container name must be lower case
            // Append a random UUID to the end of the container name so that
            // this sample can be run more than once in quick succession.
            CloudBlobContainer container = blobClient.getContainerReference(containerName);


            // Get a reference to a blob in the container
            CloudBlockBlob blob1 = container.getBlockBlobReference(inFile.getName());

            blob1.upload(new FileInputStream(inFile), inFile.length());

            System.out.println("Finished uploading");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
