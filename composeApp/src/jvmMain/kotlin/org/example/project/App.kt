package org.example.project

import kotlin.math.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.tooling.preview.Preview

import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    val points = remember { mutableStateListOf<Offset>() }
    var clickedIndex = -1
    Row {
        Canvas(Modifier.fillMaxSize().background(Color.LightGray).onPointerEvent(PointerEventType.Press) {
            if (it.button?.index == 0) {
                val pos = it.changes.first().position
                for ((i, v) in points.withIndex()) {
                    val dist = (pos.x - v.x).pow(2) + (pos.y - v.y).pow(2)
                    if (dist <= 64) {
                        clickedIndex = i
                        break
                    }
                }
                if (clickedIndex == -1)
                    points += pos
            } else if (it.button?.index == 1) {
                val pos = it.changes.first().position
                for (i in points) {
                    val dist = (pos.x - i.x).pow(2) + (pos.y - i.y).pow(2)
                    if (dist <= 800) {
                        points.remove(i)
                        break
                    }
                }
            }
        }.onPointerEvent(PointerEventType.Move) {
            if (clickedIndex == -1)
                return@onPointerEvent
            points[clickedIndex] = it.changes.first().position
        }.onPointerEvent(PointerEventType.Release) {
            if (clickedIndex == -1)
                return@onPointerEvent

            clickedIndex = -1
        }) {
            for (i in points) {
                drawCircle(Color.Magenta, 8.0f, i)
            }
            if (points.size < 3)
                return@Canvas
            var cur = points[0]
            for (i in points) {
                if (i.x < cur.x) {
                    cur = i
                } else if (i.x == cur.x && i.y > cur.y) {
                    cur = i
                }
            }
            val startPoint = cur
            var lastOffset = Offset(cur.x, cur.y - 1)
            do {
                val newPoint = findNextDot(points, lastOffset, cur)
                drawLine(Color.Black, cur, newPoint, strokeWidth = 5f)
                lastOffset = cur
                cur = newPoint
            } while (cur != startPoint)
        }
    }
}

fun findNextDot(dots: List<Offset>, lastDot: Offset, cur: Offset): Offset {
    var maxAngle = 0.0f
    var nextDot = cur
    for (i in dots) {
        val a = angle(lastDot, cur, i)
        if (a >= maxAngle) {
            maxAngle = a
            nextDot = i
        }
    }
    return nextDot
}


fun angle(a: Offset, b: Offset, c: Offset): Float {
    val vec1x = a.x - b.x
    val vec1y = a.y - b.y
    val vec2x = c.x - b.x
    val vec2y = c.y - b.y
    val ab = (vec1x * vec2x + vec1y * vec2y)
    val vec1Len = sqrt(vec1x * vec1x + vec1y * vec1y)
    val vec2Len = sqrt(vec2x * vec2x + vec2y * vec2y)
    return acos(ab / (vec1Len * vec2Len))
}