package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldLineLimits.SingleLine
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.nio.file.WatchEvent
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}


/* Composable sans etat N°1 */
@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    /*var amountInput by remember { mutableStateOf("") } *//* La variable amountInput est un etat
    car sa valeur susceptible de changer au fil du temps */
    //val amount = amountInput.toDoubleOrNull() ?: 0.0
    /* ?:  renvoie l'expression qui le précède si la valeur n'est pas null et l'expression qui le suit lorsque la valeur est null,*/
    /* alors que toDoubleOrNull() convertir le nombre saisi en nombre décimal */
    //val tip = calculateTip(amount) // tip renvoie la valeur du pourboire
    TextField(
        value = value/*amountInput*/,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        onValueChange = onValueChange /*{ amountInput = it }*/,// onValueChange rnevoi la valeur du value
        label = { Text(stringResource(label/*R.string.bill_amount*/)) },
        modifier = modifier,/*Modifier.fillMaxWidth()*/ /* Permet à TextField de remplis la largeur maximale*/
        singleLine = true,// Permet d'avoir une seule ligne pour la saisir du montant
        keyboardOptions = keyboardOptions
        /*keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next)// imeAction pour définir un bouton d'action*/
        /* permet de personnalisé le clavier  */
    )
}

/* Composable sans etat N°2*/
@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth() // la ligne prend la largeur de la taille
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.round_up_tip)
        )
        Switch(
            checked = roundUp /*roundUp*/,/*  Indique si le bouton bascule est activé. Il s'agit de
            l'état du composable Switch.*/
            onCheckedChange = onRoundUpChanged,/*Rappel à appeler en cas
            de clic sur le bouton bascule.*/
            modifier = modifier
                .fillMaxSize()
                .wrapContentWidth(Alignment.End)
        )
    }
}

/* Composable avec trois etats */
@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") } /* La variable amountInput est un etat
    car sa valeur susceptible de changer au fil du temps */
    var tipInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0 // si la valeur est null , il renvoi 0.0
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    /* ?:  renvoie l'expression qui le précède si la valeur n'est pas null et l'expression qui le suit lorsque la valeur est null,*/
    /* alors que toDoubleOrNull() convertir le nombre saisi en nombre décimal */
    val tip = calculateTip(amount, tipPercent, roundUp) // tip renvoie la valeur du pourboire

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /*Composable texte N°1*/
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        /*Zone de texte 1*/
        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),// imeAction pour définir le bouton d'action suivant */
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        /*Zone de texte 2*/
        EditNumberField(
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),// imeAction pour définir le bouton d'action terminer */
            value = tipInput,
            onValueChange = { tipInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        /* Boutton bascule */
        RoundTheTipRow(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )
        /*Composable texte N°2*/
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }

}

/**
 * Calculates the tip based on the user input and format the tip amount
 * according to the local currency.
 * Example would be "$10.00".
 */
private fun calculateTip(amount: Double,tipPercent: Double /*= 15.0*/,roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip) /* cette ligne retourne
    un nombre comme 12.5 et le convertit en "12,50 €" si l'utilisateur est en France,
    ou en "12,50 $" s'il est aux États-Unis.*/
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipCalculatorTheme {
        TipTimeLayout()
    }
}
