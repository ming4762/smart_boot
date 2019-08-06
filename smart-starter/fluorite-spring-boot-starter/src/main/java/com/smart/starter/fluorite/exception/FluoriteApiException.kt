package com.smart.starter.fluorite.exception

class FluoriteApiException : Exception {

    constructor()
    constructor(msg: String) : super(msg)
    constructor(cause: Throwable) : super(cause)
}