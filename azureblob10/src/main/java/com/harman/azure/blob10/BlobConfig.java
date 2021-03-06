package com.harman.azure.blob10;

public class BlobConfig {
    private String accountName;
    private String accountKey;
    private String containerName;
    private int uploadBufferSize = 100000000; //default 10MB

    public BlobConfig(String accountName, String accountKey, String containerName) {
        this.accountName = accountName;
        this.accountKey = accountKey;
        this.containerName = containerName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public int getUploadBufferSize() {
        return uploadBufferSize;
    }

    public void setUploadBufferSize(int uploadBufferSize) {
        this.uploadBufferSize = uploadBufferSize;
    }
}
