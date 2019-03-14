package com.harman.azure.blob10;

import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.blob.models.BlobDownloadHeaders;
import com.microsoft.azure.storage.blob.models.BlockBlobUploadResponse;
import com.microsoft.rest.v2.RestException;
import com.microsoft.rest.v2.http.HttpPipeline;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.util.Locale;

public class BlobApi {

    private BlobConfig config;
    private ContainerURL containerURL;

    public BlobApi(BlobConfig config) throws Exception {
        this.config = config;
        init();
    }

    private void init()  throws Exception {
        SharedKeyCredentials credential = new SharedKeyCredentials(config.getAccountName(), config.getAccountKey());

        HttpPipeline pipeline = StorageURL.createPipeline(credential, new PipelineOptions());

        URL u = new URL(String.format(Locale.ROOT, "https://%s.blob.core.windows.net", config.getAccountName()));

        // Create a ServiceURL object that wraps the service URL and a request pipeline.
        ServiceURL serviceURL = new ServiceURL(u, pipeline);

        containerURL = serviceURL.createContainerURL(config.getContainerName());

    }

    public void download(String blobFileName, File targetLocalFile) throws Exception {
//        DownloadResponse resp = blobURL.download().blockingGet();
//        TransferManager.downloadBlobToFile()
//        System.out.println(resp);

        BlockBlobURL blobURL = containerURL.createBlockBlobURL(blobFileName);

        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(targetLocalFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        BlobDownloadHeaders resp = TransferManager.downloadBlobToFile(fileChannel, blobURL, null, null)
                .blockingGet();

        System.out.println("Completed download request.");
        System.out.println("The blob was downloaded to " + targetLocalFile.getAbsolutePath());

    }

    public void uploadStream(File inFile) throws Exception {

        BlockBlobURL blobURL = containerURL.createBlockBlobURL(inFile.getName());

        int blockSize = config.getUploadBufferSize();
        long fileSize = inFile.length();
        int numBlocks = Math.max((int)(fileSize / blockSize), 2);

        Single response = TransferManager.uploadFromNonReplayableFlowable(Flowable.fromIterable(new FileByteBufferIterable(inFile, blockSize)), blobURL, blockSize, numBlocks, null)
                .doOnError(throwable -> {
                    if (throwable instanceof RestException) {
                        System.out.println("Failed to upload " + inFile.toPath() + " with error:" + ((RestException) throwable).response().statusCode());
                    } else {
                        System.out.println(throwable.getMessage());
                    }
                })
                .doAfterTerminate(() -> System.out.println());

//        response.subscribe(commonRestResponse -> System.out.println(commonRestResponse.toString()));
        response.blockingGet();

        System.out.println("Finished uploading stream "+inFile.getName());
    }

    public void uploadText() throws Exception {
        BlockBlobURL blobURL = containerURL.createBlockBlobURL("NewHelloWorld.txt");
        String data = "New Hello world!";
        blobURL.upload(Flowable.just(ByteBuffer.wrap(data.getBytes())), data.length())
                .blockingGet();

        System.out.println("Finished uploading text");

    }

    public void uploadFile(File inFile) throws Exception {
        //WORKING - upload a file from the disk
        BlockBlobURL blobURL = containerURL.createBlockBlobURL(inFile.getName());

        Single response = TransferManager.uploadFileToBlockBlob(
                AsynchronousFileChannel.open(inFile.toPath()), blobURL,
                BlockBlobURL.MAX_STAGE_BLOCK_BYTES, null)
                .doOnError(throwable -> {
                    if (throwable instanceof RestException) {
                        System.out.println("Failed to upload " + inFile.toPath() + " with error:" + ((RestException) throwable).response().statusCode());
                    } else {
                        System.out.println(throwable.getMessage());
                    }
                })
                .doAfterTerminate(() -> System.out.println());

        response.subscribe(commonRestResponse -> System.out.println(commonRestResponse.toString()));
        response.blockingGet();

        System.out.println("Finished uploading file "+inFile.getName());

    }
}
