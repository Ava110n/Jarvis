package org.example.project

import kotlin.math.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.tooling.preview.Preview

import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    var points by remember{mutableStateOf(listOf<Offset>())}
    Row{
        Canvas(Modifier.fillMaxSize().
        background(Color.LightGray).
        onPointerEvent(PointerEventType.Press){
            val pos = it.changes.first().position
            points += pos
        }){
            if(points.isEmpty())
                return@Canvas

            for (i in points) {
                drawCircle(Color.Magenta,8.0f,i)
            }
            var leftpoint = points[0]
            for (i in points) {
                if (i.x < leftpoint.x) {
                    leftpoint = i
                }
            }
            var lastOffset = Offset(leftpoint.x, leftpoint.y-1)
            for (i in points) {
                angle(lastOffset, leftpoint, i)
            }


        }
    }




}

fun angle(a: Offset, b: Offset, c: Offset): Float{
    val vec1x = a.x - b.x
    val vec1y = a.y - b.y
    val vec2x = c.x - b.x
    val vec2y = c.y - b.y
    val ab = (vec1x * vec2x + vec1y * vec2y)
    val vec1Len = sqrt(vec1x*vec1x + vec1y*vec1y)
    val vec2Len = sqrt(vec2x*vec2x + vec2y*vec2y)
    return acos(ab / (vec1Len * vec2Len))
}