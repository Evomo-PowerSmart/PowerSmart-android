package com.evomo.powersmart.ui.screen.monitoring_detail

import android.graphics.Canvas
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils

class CustomXAxisRenderer(
    viewPortHandler: com.github.mikephil.charting.utils.ViewPortHandler,
    xAxis: XAxis,
    trans: com.github.mikephil.charting.utils.Transformer
) : XAxisRenderer(viewPortHandler, xAxis, trans) {

    override fun drawLabel(
        c: Canvas,
        formattedLabel: String,
        x: Float,
        y: Float,
        anchor: MPPointF,
        angleDegrees: Float
    ) {
        val lines = formattedLabel.split("\n")
        lines.forEachIndexed { index, line ->
            Utils.drawXAxisValue(
                c,
                line,
                x,
                y + index * mAxisLabelPaint.textSize,
                mAxisLabelPaint,
                anchor,
                angleDegrees
            )
        }
    }
}
