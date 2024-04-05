package org.hinanawiyuzu.qixia.components

//class BottomShadowShape(private val percent: Int) : Shape {
//    override fun createOutline(
//        size: androidx.compose.ui.geometry.Size,
//        layoutDirection: LayoutDirection,
//        density: Density
//    ): Outline {
//        val shape = RoundedCornerShape(percent = percent)
//        return when (val outline = shape.createOutline(size, layoutDirection, density)) {
//            is Outline.Generic -> {
//                Outline.Generic(Path().apply {
//                    addPath(outline.path)
//                    lineTo(size.width, size.height - 1f)
//                    lineTo(0f, size.height - 1f)
//                    close()
//                })
//            }
//            else -> outline
//        }
//    }
//}