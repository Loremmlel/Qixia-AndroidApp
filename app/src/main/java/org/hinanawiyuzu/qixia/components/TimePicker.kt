package org.hinanawiyuzu.qixia.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TimePicker(
  hour: MutableState<Int>,
  minute: MutableState<Int>
) {
  val itemHeight = 50.dp
  Box(
    modifier = Modifier
      .wrapContentHeight()
      .fillMaxWidth(),
    Alignment.Center
  ) {
    Row(
      Modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      //  小时
      val hours = ArrayList<Pair<Int, String>>(24).apply {
        for (i in 0..23) {
          add(Pair(i, "${i}时"))
        }
      }
      DatePickerColumn(hours, itemHeight, 50.dp, hour)
      //  分
      val minutes = ArrayList<Pair<Int, String>>(60).apply {
        for (i in 0..59) {
          add(Pair(i, "${i}分"))
        }
      }
      DatePickerColumn(minutes, itemHeight, 50.dp, minute)
    }
    //  放在后面使得不会被遮住
    Column {
      Divider(
        Modifier.padding(
          start = 15.dp,
          end = 15.dp,
          bottom = itemHeight
        ),
        thickness = 1.dp
      )
      Divider(
        Modifier.padding(
          start = 15.dp,
          end = 15.dp
        ),
        thickness = 1.dp
      )
    }
  }
}

@Composable
fun DatePickerColumn(
  //	列表
  pairList: List<Pair<Int, String>>,
  itemHeight: Dp,
  itemWidth: Dp? = null,
  valueState: MutableState<Int>,
  focusColor: Color = MaterialTheme.colorScheme.primary,
  unfocusedColor: Color = Color(0xFFC5C7CF)
) {
  val dataPickerCoroutineScope = rememberCoroutineScope()
  val listState = rememberLazyListState()
  var value by valueState
  LazyColumn(
    state = listState,
    modifier = Modifier
      .height(itemHeight * 6)
      .padding(top = itemHeight / 2, bottom = itemHeight / 2)
  ) {
    item {
      Surface(Modifier.height(itemHeight)) {}
    }
    item {
      Surface(Modifier.height(itemHeight)) {}
    }
    itemsIndexed(items = pairList, key = { _, pair -> pair.first }) { index, pair ->
      val widthModifier = itemWidth?.let { Modifier.width(itemWidth) } ?: Modifier
      Box(
        modifier = Modifier
          .height(itemHeight)
          .then(widthModifier)
          .clickable {
            dataPickerCoroutineScope.launch {
              listState.animateScrollToItem(index = index)
            }
          }
          .padding(start = 5.dp, end = 5.dp), Alignment.Center
      ) {
        Text(
          text = pair.second, color =
          if (listState.firstVisibleItemIndex == index) focusColor
          else unfocusedColor
        )
      }
    }
    item {
      Surface(Modifier.height(itemHeight)) {}
    }
    item {
      Surface(Modifier.height(itemHeight)) {}
    }
  }
  /**
   * Jetpack Compose LazyColumn的滑动开始、结束及进行中事件
   */
  if (listState.isScrollInProgress) {
    LaunchedEffect(Unit) {
      //只会调用一次，相当于滚动开始
    }
    //当state处于滚动时，preScrollStartOffset会被初始化并记忆,不会再被更改
//        val preScrollStartOffset by remember { mutableIntStateOf(listState.firstVisibleItemScrollOffset) }
//        val preItemIndex by remember { mutableIntStateOf(listState.firstVisibleItemIndex) }
//        val isScrollDown = if (listState.firstVisibleItemIndex > preItemIndex) {
//            //第一个可见item的index大于开始滚动时第一个可见item的index，说明往下滚动了
//            true
//        } else if (listState.firstVisibleItemIndex < preItemIndex) {
//            //第一个可见item的index小于开始滚动时第一个可见item的index，说明往上滚动了
//            false
//        } else {
//            //第一个可见item的index等于开始滚动时第一个可见item的index,对比item offset
//            listState.firstVisibleItemScrollOffset > preScrollStartOffset
//        }
    DisposableEffect(Unit) {
      onDispose {
        //	滑动结束时给状态赋值，并自动对齐
        value = pairList[listState.firstVisibleItemIndex].first
        dataPickerCoroutineScope.launch {
          listState.animateScrollToItem(listState.firstVisibleItemIndex)
        }
      }
    }
  }
  //  选择初始值
  LaunchedEffect(Unit) {
    var initIndex = 0
    for (index in pairList.indices) {
      if (value == pairList[index].first) {
        initIndex = index
        break
      }
    }
    dataPickerCoroutineScope.launch {
      listState.animateScrollToItem(initIndex)
    }
  }
}