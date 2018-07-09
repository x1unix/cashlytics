package com.x1unix.cashlytics.core.providers.internals

import com.x1unix.cashlytics.core.providers.MessageProcessor

interface MessageProcessorConsumer {
    var messageProcessor: MessageProcessor
}