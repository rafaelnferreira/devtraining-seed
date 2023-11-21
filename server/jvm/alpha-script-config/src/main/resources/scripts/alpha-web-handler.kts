webHandlers {

    val tmp = Files.createTempDirectory("test")

    multipartEndpoint("file-upload") {
        handleRequest {
            body.fileUploads.forEach {
                it.copyTo(tmp.resolve(it.fileName))
            }
        }
    }

    endpoint(GET, "all-instruments") {
        handleRequest {
            db.getBulk(INSTRUMENT)
        }
    }
}