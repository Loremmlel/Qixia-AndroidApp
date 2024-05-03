package org.hinanawiyuzu.qixia.exception

class AlarmSetFailedException : RuntimeException {
  constructor() : super()
  constructor(message: String) : super(message)
  constructor(message: String, cause: Throwable) : super(message, cause)
}