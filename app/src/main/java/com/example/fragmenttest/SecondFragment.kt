package com.example.fragmenttest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SecondFragment : Fragment() {
    private var arr = arrayOf(
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf("26.12.2023")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedInstanceState?.let {
            val arrStr = it.getStringArray("arr")
            for (i in 0..4) {
                arr[i].value = arrStr!![i]
            }
        }
        return ComposeView(
            requireContext()).apply {
            setContent{
                secondScreen(::comeBack, arr)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondFragment()
    }

    fun comeBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed();
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray("arr",
            arrayOf(arr[0].value, arr[1].value, arr[2].value, arr[3].value, arr[4].value))
    }
}


data class PreviewOption(val text: String, val id: Int)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun secondScreen(comeback: () -> Unit, arr: Array<MutableState<String>>) {
    val options1 = remember {
        mutableStateOf(listOf<PreviewOption>())
    }
    val options2 = remember {
        mutableStateOf(listOf<PreviewOption>())
    }

    options1.value = listOf(
        PreviewOption("Автомобиль", 1),
        PreviewOption("Отдых и развлечения", 2),
        PreviewOption("Продукты", 3),
        PreviewOption("Кафе и ресторан", 4),
        PreviewOption("Одежда", 5),
        PreviewOption("Здоровье и фитнес", 6),
        PreviewOption("Подарки", 7),
        PreviewOption("Общественный транспорт", 8),
    )
    options2.value = listOf(
        PreviewOption("Наличные (200 ₽)", 1),
        PreviewOption("Карта 894 (20000 ₽)", 2),
        PreviewOption("Банковский счёт (2000000 ₽)", 3),
    )

    Box {
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.background2),
            contentDescription = "Фон",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = comeback,
                    contentPadding = PaddingValues(0.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.width(30.dp)
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(R.drawable.back),
                        contentDescription = "Назад",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.height(30.dp)
                    )
                }

                Button(
                    onClick = comeback,
                    contentPadding = PaddingValues(0.dp),
                    border = BorderStroke(1.dp, Color.DarkGray),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp)
                ) {
                    Text(
                        stringResource(R.string.save),
                        fontSize = 5.em,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(25.dp, 10.dp, 25.dp, 10.dp)
                    )
                }
            }

            Column (modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 0.dp)) {
                Text(stringResource(R.string.category), fontSize = 4.em, color = Color.DarkGray)


                var selectedOption by remember {
                    mutableStateOf<PreviewOption?>(if (arr[0].value == "")
                        null
                    else
                        options1.value[arr[0].value.toInt() - 1])
                }
                TextFieldMenu(
                    label = "",
                    options = options1.value,
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it; if (it != null) arr[0].value = it.id.toString() },
                    optionToString = { it.text },
                    filteredOptions = { searchInput ->
                        options1.value.filter { it.text.contains(searchInput, ignoreCase = true) }
                    },
                )
            }

            Column (modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 0.dp)) {
                Text(stringResource(R.string.comment), fontSize = 4.em, color = Color.DarkGray)

                val message = remember{ arr[1] }
                TextField(
                    value = message.value,
                    onValueChange = {newText -> message.value = newText},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp, 0.dp, 0.dp),
                    textStyle = TextStyle(fontSize=4.5.em),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
                    singleLine = true
                )
            }

            Column (modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 0.dp)) {
                Text(stringResource(R.string.bank_account), fontSize = 4.em, color = Color.DarkGray)

                var selectedOption by remember {
                    mutableStateOf<PreviewOption?>(if (arr[2].value == "")
                        null
                    else
                        options2.value[arr[2].value.toInt() - 1])
                }
                TextFieldMenu(
                    label = "",
                    options = options2.value,
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it; if (it != null) arr[2].value = it.id.toString() },
                    optionToString = { it.text },
                    filteredOptions = { searchInput ->
                        options2.value.filter { it.text.contains(searchInput, ignoreCase = true) }
                    },
                )
            }

            Column (modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 0.dp)) {
                Text(stringResource(R.string.summa), fontSize = 4.em, color = Color.DarkGray)

                val message = remember{ arr[3] }
                TextField(
                    value = message.value,
                    onValueChange = {newText -> message.value = newText},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp, 0.dp, 0.dp),
                    textStyle = TextStyle(fontSize=4.5.em),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }

            val calendar = Calendar.getInstance()
            val arrInt = arr[4].value.split(".").map { it.toInt() }
            calendar.set(arrInt[2], arrInt[1], arrInt[0])
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
            var showDatePicker by remember {
                mutableStateOf(false)
            }
            var selectedDate by remember {
                mutableLongStateOf(calendar.timeInMillis) // or use mutableStateOf(calendar.timeInMillis)
            }
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                            selectedDate = datePickerState.selectedDateMillis!!
                        }) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }
            Column (modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 0.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.date),
                        fontSize = 4.em,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                    )

                    Button(
                        onClick = {
                            showDatePicker = true
                        },
                        contentPadding = PaddingValues(0.dp),
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier.size(30.dp)
                    ) {
                        Image(
                            bitmap = ImageBitmap.imageResource(R.drawable.calendar),
                            contentDescription = "Выбор даты",
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.height(30.dp)
                        )
                    }
                }

                val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
                val boof = formatter.format(Date(selectedDate))
                arr[4].value = boof
                TextField(
                    value = boof,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = TextStyle(fontSize=4.5.em),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
                    singleLine = true,
                    readOnly = true
                )
            }

        }
    }

}
