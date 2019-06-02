package com.x1unix.cashlytics.core.exceptions

import java.lang.Exception

class ProviderNotFoundException(val providerName: String): Exception("No data provider for '${providerName}") {}