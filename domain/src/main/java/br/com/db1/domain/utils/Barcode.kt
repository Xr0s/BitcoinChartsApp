package br.com.dmcard.contadigital.domain.utils

import br.com.dmcard.contadigital.domain.utils.Barcode.CodeType.BARCODE
import br.com.dmcard.contadigital.domain.utils.Barcode.CodeType.NUMERIC_REPRESENTATION
import br.com.dmcard.contadigital.domain.utils.Barcode.PaymentType.BILL
import br.com.dmcard.contadigital.domain.utils.Barcode.PaymentType.REGULAR
import java.util.*

class Barcode private constructor(
    sequence: String,
    private val type: CodeType,
    private val paymentType: PaymentType
) {

    private val sequence: String

    val value: Double?
        get() = when (type) {
            BARCODE -> when (paymentType) {
                REGULAR -> sequence.substring(9, 19).toDouble() / 100
                BILL -> sequence.substring(4, 15).toDouble() / 100
            }
            else -> when (paymentType) {
                REGULAR -> sequence.substring(37, 47).toDouble() / 100
                BILL -> strippedServiceBillNumericRepresentation(sequence)
                    .substring(4, 15).toDouble() / 100
            }
        }


    val dueDate: Date?
        get() {
            var dueDateFactor = 0

            if (type == BARCODE && paymentType == REGULAR)
                dueDateFactor = Integer.parseInt(sequence.substring(5, 9))
            else if (type == NUMERIC_REPRESENTATION && paymentType == REGULAR)
                dueDateFactor = Integer.parseInt(sequence.substring(33, 37))

            return if (dueDateFactor != 0) dueDate(dueDateFactor) else null
        }

    val companyCode: String
        get() = when {
            type == BARCODE -> when (paymentType) {
                BILL -> sequence.substring(15, 19)
                else -> sequence.substring(0, 3)
            }
            paymentType == BILL -> sequence.substring(16, 20)
            else -> sequence.substring(0, 3)
        }


    init {
        this.sequence = padIfNeeded(sequence, type)
    }

    fun numericRepresentation(): String {
        if (type == NUMERIC_REPRESENTATION) return sequence
        return if (paymentType == REGULAR) regularNumericRepresentation()
        else serviceBillNumericRepresentation()
    }

    private fun regularNumericRepresentation(): String {
        val barcode = sequence
        val freeField = barcode.substring(19)

        val firstField = barcode.substring(0, 4) + freeField.substring(0, 5)
        val firstCheckDigit = mod10(firstField)

        val secondField = freeField.substring(5, 15)
        val secondCheckDigit = mod10(secondField)

        val thirdField = freeField.substring(15, 25)
        val thirdCheckDigit = mod10(thirdField)

        val checkDigit = barcode.substring(4, 5)
        val dueDateFactor = barcode.substring(5, 9)
        val value = barcode.substring(9, 19)

        return firstField + firstCheckDigit + secondField + secondCheckDigit +
                thirdField + thirdCheckDigit + checkDigit + dueDateFactor + value

    }

    private fun serviceBillNumericRepresentation(): String {
        val firstField = sequence.substring(0, 11)
        val secondField = sequence.substring(11, 22)
        val thirdField = sequence.substring(22, 33)
        val fourthField = sequence.substring(33, 44)

        val firstCheckDigit: Int
        val secondCheckDigit: Int
        val thirdCheckDigit: Int
        val fourthCheckDigit: Int

        if (sequence[2] == '6' || sequence[2] == '7') {
            firstCheckDigit = mod10(firstField)
            secondCheckDigit = mod10(secondField)
            thirdCheckDigit = mod10(thirdField)
            fourthCheckDigit = mod10(fourthField)
        } else {
            firstCheckDigit = mod11(firstField, 0)
            secondCheckDigit = mod11(secondField, 0)
            thirdCheckDigit = mod11(thirdField, 0)
            fourthCheckDigit = mod11(fourthField, 0)

        }

        return (firstField + firstCheckDigit + secondField + secondCheckDigit
                + thirdField + thirdCheckDigit + fourthField + fourthCheckDigit)

    }

    private fun padIfNeeded(sequence: String, type: CodeType): String {
        var newSequence = sequence

        if (type == NUMERIC_REPRESENTATION && sequence.length < 47) {
            val newZeros = 47 - sequence.length
            newSequence = newSequence.padEnd(newZeros, '0')
        }

        return newSequence
    }


    internal enum class CodeType {
        NUMERIC_REPRESENTATION,
        BARCODE
    }

    internal enum class PaymentType {
        REGULAR,
        BILL
    }

    companion object {

        const val REGULAR_MASK = "#####.##### #####.###### #####.###### # ##############"
        const val SERVICE_BILL_MASK = "###########-# ###########-# ###########-# ###########-#"
        const val BARCODE_LENGTH = 44
        private val companyNameManager = ""

        fun withBarcode(barcodeString: String): Barcode? {
            val serviceBill =
                barcodeString.length == BARCODE_LENGTH && barcodeString.startsWith("8")
            val paymentType = if (serviceBill) BILL else REGULAR
            var barcode: Barcode? = null

            if (barcodeString.length == 44 && validBarcode(
                    barcodeString,
                    BARCODE, paymentType
                )
            )
                barcode = Barcode(barcodeString, BARCODE, paymentType)

            return barcode
        }

        fun withNumericRepresentation(numericRepresentation: String): Barcode? {
            val serviceBill =
                numericRepresentation.length == 48 && numericRepresentation.startsWith("8")

            val paymentType = if (serviceBill) BILL else REGULAR
            var barcode: Barcode? = null

            if (numericRepresentation.length >= 36 && validBarcode(
                    numericRepresentation,
                    NUMERIC_REPRESENTATION,
                    paymentType
                )
            )
                barcode = Barcode(
                    numericRepresentation,
                    NUMERIC_REPRESENTATION, paymentType
                )

            return barcode

        }

        private fun validBarcode(
            barcode: String,
            type: CodeType,
            paymentType: PaymentType
        ): Boolean {

            if (type == BARCODE)
                return if (paymentType == REGULAR)
                    validBarcode(barcode)
                else
                    validServiceBillBarcode(barcode)

            return if (paymentType == REGULAR)
                validNumericRepresentation(barcode)
            else
                validServiceBillNumericRepresentation(barcode)
        }

        private fun validServiceBillBarcode(barcodeString: String): Boolean {
            val checkDigit = Integer.parseInt(barcodeString.substring(3, 4))
            val barCodeToValidate = StringBuilder(barcodeString).deleteCharAt(3).toString()
            val calculatedDigit: Int
            if (barcodeString[2] == '6' || barcodeString[2] == '7')
                calculatedDigit = mod10(barCodeToValidate)
            else
                calculatedDigit = mod11(barCodeToValidate, 0)
            return checkDigit == calculatedDigit
        }

        private fun validServiceBillNumericRepresentation(numericString: String): Boolean {
            val firstBlock = numericString.substring(0, 11)
            val firstBlockCheckDigit = Integer.parseInt(numericString.substring(11, 12))

            val secondBlock = numericString.substring(12, 23)
            val secondBlockCheckDigit = Integer.parseInt(numericString.substring(23, 24))

            val thirdBlock = numericString.substring(24, 35)
            val thirdBlockCheckDigit = Integer.parseInt(numericString.substring(35, 36))

            val fourthBlock = numericString.substring(36, 47)
            val fourthBlockCheckDigit = Integer.parseInt(numericString.substring(47, 48))

            val calculatedFirstCheckDigit: Int
            val calculatedSecondCheckDigit: Int
            val calculatedThirdCheckDigit: Int
            val calculatedFourthCheckDigit: Int


            if (numericString[2] == '6' || numericString[2] == '7') {
                calculatedFirstCheckDigit = mod10(firstBlock)
                calculatedSecondCheckDigit = mod10(secondBlock)
                calculatedThirdCheckDigit = mod10(thirdBlock)
                calculatedFourthCheckDigit = mod10(fourthBlock)
            } else {
                calculatedFirstCheckDigit = mod11(firstBlock, 0)
                calculatedSecondCheckDigit = mod11(secondBlock, 0)
                calculatedThirdCheckDigit = mod11(thirdBlock, 0)
                calculatedFourthCheckDigit = mod11(fourthBlock, 0)
            }

            return firstBlockCheckDigit == calculatedFirstCheckDigit &&
                    secondBlockCheckDigit == calculatedSecondCheckDigit &&
                    thirdBlockCheckDigit == calculatedThirdCheckDigit &&
                    fourthBlockCheckDigit == calculatedFourthCheckDigit
        }

        private fun validBarcode(barcodeString: String): Boolean {
            val checkDigit = Integer.parseInt(barcodeString.substring(4, 5))
            val barCodeToValidate = StringBuilder(barcodeString).deleteCharAt(4).toString()
            val calculatedDigit = mod11(barCodeToValidate, 1)
            return checkDigit == calculatedDigit
        }

        private fun validNumericRepresentation(numericString: String): Boolean {
            val firstBlock = numericString.substring(0, 9)
            val firstBlockCheckDigit = Integer.parseInt(numericString.substring(9, 10))

            val secondBlock = numericString.substring(10, 20)
            val secondBlockCheckDigit = Integer.parseInt(numericString.substring(20, 21))

            val thirdBlock = numericString.substring(21, 31)
            val thirdBlockCheckDigit = Integer.parseInt(numericString.substring(31, 32))

            val calculatedFirstCheckDigit = mod10(firstBlock)
            val calculatedSecondCheckDigit = mod10(secondBlock)
            val calculatedThirdCheckDigit = mod10(thirdBlock)

            return firstBlockCheckDigit == calculatedFirstCheckDigit &&
                    secondBlockCheckDigit == calculatedSecondCheckDigit &&
                    thirdBlockCheckDigit == calculatedThirdCheckDigit
        }

        private fun strippedServiceBillNumericRepresentation(numericRepresentation: String): String {
            val firstBlock = numericRepresentation.substring(0, 11)
            val secondBlock = numericRepresentation.substring(12, 23)
            val thirdBlock = numericRepresentation.substring(24, 35)
            val fourthBlock = numericRepresentation.substring(36, 47)

            return firstBlock + secondBlock + thirdBlock + fourthBlock
        }

        private fun dueDate(daysSinceBaseDate: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.set(1997, Calendar.OCTOBER, 7, 0, 0, 0)
            calendar.add(Calendar.DATE, daysSinceBaseDate)
            return calendar.time
        }

        private fun mod11(string: String, defaultValue: Int): Int {
            val factors = ArrayList(listOf(2, 3, 4, 5, 6, 7, 8, 9))
            var sum = 0

            val reversedBarcode = StringBuilder(string).reverse().toString()
            val digits = toIntArray(reversedBarcode)

            for (number in digits) {
                sum += number * factors[0]
                factors.add(factors[0])
                factors.removeAt(0)
            }

            val mod = sum % 11
            val returnValue = 11 - mod
            return if (returnValue >= 10) defaultValue else returnValue
        }

        private fun mod10(string: String): Int {
            val factors = ArrayList(listOf(2, 1))
            var sum = 0

            val reversedBarcode = StringBuilder(string).reverse().toString()
            val digits = toIntArray(reversedBarcode)

            for (number in digits) {
                val product = number * factors[0]
                val productString = product.toString()
                var productSum = 0
                val productDigits = toIntArray(productString)

                for (digit in productDigits)
                    productSum += digit

                sum += productSum

                factors.add(factors.removeAt(0))
            }

            val mod = sum % 10
            return if (mod == 0) 0 else 10 - mod
        }

        private fun toIntArray(string: String): IntArray {
            val digits = IntArray(string.length)

            for (i in string.indices) {
                val number = string.substring(i, i + 1)
                digits[i] = Integer.parseInt(number)
            }

            return digits
        }
    }
}
