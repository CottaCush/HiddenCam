package com.cottacush.android.hiddencam

sealed class CaptureTimeFrequency {
    object OneShot : CaptureTimeFrequency()
    class Recurring(val captureIntervalMillis: Long) : CaptureTimeFrequency()
}