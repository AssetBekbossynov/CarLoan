package kz.assetbekbossynov.carloan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.input_fields.*
import retrofit2.Response
import com.redmadrobot.inputmask.MaskedTextChangedListener
import java.util.*
import kotlinx.android.synthetic.main.input_fields.zip as zipInput
import kotlinx.android.synthetic.main.input_fields.state as stateInput
import kotlinx.android.synthetic.main.input_fields.city as cityInput
import kotlinx.android.synthetic.main.input_fields.address as addressInput
import kotlinx.android.synthetic.main.input_fields.email as emailInput
import kotlinx.android.synthetic.main.input_fields.military as militaryInput
import kotlinx.android.synthetic.main.input_fields.employer as employerInput
import kotlinx.android.synthetic.main.input_fields.ssn as ssnInput
import kotlinx.android.synthetic.main.toolbar.title as titleLayout
import android.text.*
import android.widget.*
import android.text.format.Formatter
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.toolbar.*
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.expandable_item.view.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat


class QuestionnaireActivity : AppCompatActivity() {

    internal lateinit var requestedAmount : TextInputLayout
    internal lateinit var firstName: TextInputLayout
    internal lateinit var lastName: TextInputLayout
    internal lateinit var zip: TextInputLayout
    internal lateinit var state: TextInputLayout
    internal lateinit var city: TextInputLayout
    internal lateinit var address: TextInputLayout
    internal lateinit var addressSince: TextInputLayout
    internal lateinit var birthDate: TextInputLayout
    internal lateinit var email : TextInputLayout
    internal lateinit var ownHome: TextInputLayout
    internal lateinit var homePhone: TextInputLayout
    internal lateinit var workPhone: TextInputLayout
    internal lateinit var military: TextInputLayout
    internal lateinit var timeToCall: TextInputLayout
    internal lateinit var employer: TextInputLayout
    internal lateinit var jobTitle: TextInputLayout
    internal lateinit var employedMonth: TextInputLayout
    internal lateinit var monthlyIncome: TextInputLayout
    internal lateinit var payDate1: TextInputLayout
    internal lateinit var payDate2: TextInputLayout
    internal lateinit var payFrequency: TextInputLayout
    internal lateinit var driversLicense: TextInputLayout
    internal lateinit var driversLicenseState: TextInputLayout
    internal lateinit var bankName: TextInputLayout
    internal lateinit var bankPhone: TextInputLayout
    internal lateinit var bankAba: TextInputLayout
    internal lateinit var bankAccount: TextInputLayout
    internal lateinit var bankAccountType: TextInputLayout
    internal lateinit var directDeposit: TextInputLayout
    internal lateinit var ssn: TextInputLayout
    internal lateinit var bankAccountSince: TextInputLayout
    internal lateinit var incomeType: TextInputLayout

    private var requestedAmountFilled = false
    private var firstNameFilled = false
    private var lastNameFilled = false
    private var zipFilled = false
    private var stateFilled = false
    private var cityFilled = false
    private var addressFilled = false
    private var addressSinceFilled = false
    private var birthDateFilled = false
    private var emailFilled = false
    private var ownHomeFilled = false
    private var homePhoneFilled = false
    private var workPhoneFilled = false
    private var militaryFilled = false
    private var timeToCallFilled = false
    private var employerFilled = false
    private var jobTitleFilled = false
    private var employedMonthFilled = false
    private var monthlyIncomeFilled = false
    private var payDate1Filled = false
    private var payDate2Filled = false
    private var payFrequencyFilled = false
    private var driversLicenseFilled = false
    private var driversLicenseStateFilled = false
    private var bankNameFilled = false
    private var bankPhoneFilled = false
    private var bankAbaFilled = false
    private var bankAccountFilled = false
    private var bankAccountTypeFilled = false
    private var directDepositFilled = false
    private var ssnFilled = false
    private var bankAccountSinceFilled = false
    private var incomeTypeFilled = false

    internal lateinit var timeToCallList: ArrayList<String>
    internal lateinit var payFrequencyList: ArrayList<String>
    internal lateinit var bankAccountTypeList: ArrayList<String>
    internal lateinit var incomeTypeList: ArrayList<String>
    internal lateinit var residenceStatusList: ArrayList<String>
    internal lateinit var militaryStatusList: ArrayList<String>
    internal lateinit var depositStatusList: ArrayList<String>

    internal var dialog: AlertDialog? = null
    internal var adapter: DialogListAdapter? = null

    lateinit var client_ip_address: String
    lateinit var tier_key: String
    lateinit var session_id: String
    internal var payday = false

    var max: Int = 0
    var min: Int = 100

    var militaryStatus = ""
    var residence = ""
    var depositStatus = ""

    var text = ""

    val source = "abcdefghijklmnopqrstuvwxyz0123456789"

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_fields)

        Fabric.with(this, Crashlytics())

        titleLayout.text = "Questionnaire"

        backbutton.setOnClickListener {
            onBackPressed()
        }

        policy.setOnClickListener {
            val intent = Intent(this, PolicyActivity::class.java)
            startActivity(intent)
        }

        timeToCallList = ArrayList(Arrays.asList(*resources.getStringArray(R.array.time_to_call_list)))
        payFrequencyList = ArrayList(Arrays.asList(*resources.getStringArray(R.array.pay_frequencies)))
        bankAccountTypeList = ArrayList(Arrays.asList(*resources.getStringArray(R.array.bank_account_types)))
        incomeTypeList = ArrayList(Arrays.asList(*resources.getStringArray(R.array.income_types)))
        residenceStatusList = ArrayList(Arrays.asList(*resources.getStringArray(R.array.residence_status)))
        militaryStatusList = ArrayList(Arrays.asList(*resources.getStringArray(R.array.military_status)))
        depositStatusList = ArrayList(Arrays.asList(*resources.getStringArray(R.array.deposit_status)))

        val wm = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        client_ip_address = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

        val intent = intent

        if (intent.getStringExtra("btn") == "installment"){
            tier_key = BuildConfig.TIER_KEY_INST
            payday = false
            max = 3000
        }else if (intent.getStringExtra("btn") == "payday"){
            tier_key = BuildConfig.TIER_KEY_PD
            payday = true
            max = 1500
        }

        next.setOnClickListener {
            if(next.isEnabled){
                Answers.getInstance().logCustom(CustomEvent("SEND_BTN_" + session_id)
                        .putCustomAttribute("requestedAmount", requestedAmount.editText?.text!!.toString())
                        .putCustomAttribute("employer", employer.editText?.text!!.toString())
                        .putCustomAttribute("jobTitle", jobTitle.editText?.text!!.toString())
                        .putCustomAttribute("employedMonth", employedMonth.editText?.text!!.toString())
                        .putCustomAttribute("monthlyIncome", monthlyIncome.editText?.text!!.toString())
                        .putCustomAttribute("payDate1", payDate1.editText?.text!!.toString())
                        .putCustomAttribute("payDate2", payDate2.editText?.text!!.toString())
                        .putCustomAttribute("payFrequency", payFrequency.editText?.text!!.toString())
                        .putCustomAttribute("driversLicense", driversLicense.editText?.text!!.toString())
                        .putCustomAttribute("bankName", bankName.editText?.text!!.toString())
                        .putCustomAttribute("bankPhone", bankPhone.editText?.text!!.toString())
                        .putCustomAttribute("bankAba", bankAba.editText?.text!!.toString())
                        .putCustomAttribute("bankAccount", bankAccount.editText?.text!!.toString())
                        .putCustomAttribute("bankAccountType", bankAccountType.editText?.text!!.toString())
                        .putCustomAttribute("depositStatus", depositStatus)
                        .putCustomAttribute("firstName", firstName.editText?.text!!.toString())
                        .putCustomAttribute("lastName", lastName.editText?.text!!.toString())
                        .putCustomAttribute("ssn", ssn.editText?.text!!.toString())
                        .putCustomAttribute("birthDate", birthDate.editText?.text!!.toString())
                        .putCustomAttribute("residence", residence)
                        .putCustomAttribute("address", address.editText?.text!!.toString())
                        .putCustomAttribute("city", city.editText?.text!!.toString())
                        .putCustomAttribute("state", state.editText?.text!!.toString())
                        .putCustomAttribute("zip", zip.editText?.text!!.toString())
                        .putCustomAttribute("email", email.editText?.text!!.toString())
                        .putCustomAttribute("homePhone", homePhone.editText?.text!!.toString())
                        .putCustomAttribute("workPhone", workPhone.editText?.text!!.toString())
                        .putCustomAttribute("timeToCall", timeToCall.editText?.text!!.toString())
                        .putCustomAttribute("militaryStatus", militaryStatus)
                        .putCustomAttribute("addressSince", addressSince.editText?.text!!.toString())
                        .putCustomAttribute("bankAccountSince", bankAccountSince.editText?.text!!.toString())
                        .putCustomAttribute("incomeType", incomeType.editText?.text!!.toString())
                        .putCustomAttribute("session_id", session_id)
                        .putCustomAttribute("client_ip_address", client_ip_address))

                sendData()
            }else{
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        session_id = ""
        for(i in 0..32){
            session_id += source[Random().nextInt(source.length)].toString()
        }

        initializeViews()
    }

    fun initializeViews(){
        requestedAmount = requested_amount.findViewById(R.id.value)
        firstName = first_name.findViewById(R.id.value)
        lastName = last_name.findViewById(R.id.value)
        zip = zipInput.findViewById(R.id.value)
        state = stateInput.findViewById(R.id.value)
        city = cityInput.findViewById(R.id.value)
        address = addressInput.findViewById(R.id.value)
        addressSince = address_since.findViewById(R.id.value)
        birthDate = birth_date.findViewById(R.id.value)
        email  = emailInput.findViewById(R.id.value)
        ownHome = own_home.findViewById(R.id.value)
        homePhone = home_phone.findViewById(R.id.value)
        workPhone = work_phone.findViewById(R.id.value)
        military = militaryInput.findViewById(R.id.value)
        timeToCall = time_to_call.findViewById(R.id.value)
        employer = employerInput.findViewById(R.id.value)
        jobTitle = job_title.findViewById(R.id.value)
        employedMonth = employed_month.findViewById(R.id.value)
        monthlyIncome = monthly_income.findViewById(R.id.value)
        payDate1 = pay_date1.findViewById(R.id.value)
        payDate2 = pay_date2.findViewById(R.id.value)
        payFrequency = pay_frequency.findViewById(R.id.value)
        driversLicense = driver_license.findViewById(R.id.value)
        driversLicenseState = driver_license_state.findViewById(R.id.value)
        bankName = bank_name.findViewById(R.id.value)
        bankPhone = bank_phone.findViewById(R.id.value)
        bankAba = bank_aba.findViewById(R.id.value)
        bankAccount = bank_account.findViewById(R.id.value)
        bankAccountType = bank_account_type.findViewById(R.id.value)
        directDeposit = direct_deposit.findViewById(R.id.value)
        ssn = ssnInput.findViewById(R.id.value)
        bankAccountSince = bank_account_since.findViewById(R.id.value)
        incomeType = income_type.findViewById(R.id.value)

        requestedAmount.hint = "Requested amount"
        firstName.hint = "First name"
        lastName.hint = "Last name"
        zip.hint = "ZIP code"
        state.hint = "State(abbreviation)"
        city.hint = "City"
        address.hint = "Address"
        addressSince.hint = "Address duration(month)"
        birthDate.hint = "Birth date(YYYY-MM-DD)"
        email .hint = "Email"
        ownHome.hint = "Residence status"
        homePhone.hint = "Home phone number"
        workPhone.hint = "Work phone number"
        military.hint = "Military status"
        timeToCall.hint = "Time to call"
        employer.hint = "Employer name"
        jobTitle.hint = "Job title"
        employedMonth.hint = "Employed for(month)"
        monthlyIncome.hint = "Monthly income"
        payDate1.hint = "Pay date 1(YYYY-MM-DD)"
        payDate2.hint = "Pay date 2(YYYY-MM-DD)"
        payFrequency.hint = "Pay frequency"
        driversLicense.hint = "Drivers license number"
        driversLicenseState.hint = "Drivers license state(abbreviation)"
        bankName.hint = "Bank name"
        bankPhone.hint = "Bank phone number"
        bankAba.hint = "Bank ABA"
        bankAccount.hint = "Bank account number"
        bankAccountType.hint = "Bank account type"
        directDeposit.hint = "Deposit type"
        ssn.hint = "Social security number"
        bankAccountSince.hint = "Bank account duration(month)"
        incomeType.hint = "Income type"

        requestedAmount.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        firstName.editText?.inputType = InputType.TYPE_CLASS_TEXT and InputType.TYPE_TEXT_FLAG_MULTI_LINE.inv() or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        lastName.editText?.inputType = InputType.TYPE_CLASS_TEXT and InputType.TYPE_TEXT_FLAG_MULTI_LINE.inv() or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        zip.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        addressSince.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        bankAccountSince.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        state.editText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        email.editText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        homePhone.editText?.inputType = InputType.TYPE_CLASS_PHONE
        workPhone.editText?.inputType = InputType.TYPE_CLASS_PHONE
        employedMonth.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        monthlyIncome.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        driversLicenseState.editText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        bankPhone.editText?.inputType = InputType.TYPE_CLASS_PHONE
        bankName.editText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        bankAba.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        bankAccount.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        ssn.editText?.inputType = InputType.TYPE_CLASS_NUMBER

        firstName.editText?.filters = createMaxLengthFilter(128)
        lastName.editText?.filters = createMaxLengthFilter(128)
        zip.editText?.filters = createMaxLengthFilter(5)
        state.editText?.filters = createMaxLengthFilter(2)
        driversLicenseState.editText?.filters = createMaxLengthFilter(2)
        bankAba.editText?.filters = createMaxLengthFilter(9)
        addressSince.editText?.filters = createMaxLengthFilter(3)
        bankAccountSince.editText?.filters = createMaxLengthFilter(2)
        employedMonth.editText?.filters = createMaxLengthFilter(2)

        homePhone.editText?.let {
            it.addTextChangedListener(MaskedTextChangedListener(
                    "([000]) [000] [0000]",
                    true,
                    it, null, null
            ))
        }

        workPhone.editText?.let {
            it.addTextChangedListener(MaskedTextChangedListener(
                    "([000]) [000] [0000]",
                    true,
                    it, null, null
            ))
        }

        bankPhone.editText?.let {
            it.addTextChangedListener(MaskedTextChangedListener(
                    "([000]) [000] [0000]",
                    true,
                    it, null, null
            ))
        }

        val watcher = SingleWatcher()
        requestedAmount.editText?.addTextChangedListener(watcher)
        firstName.editText?.addTextChangedListener(watcher)
        lastName.editText?.addTextChangedListener(watcher)
        zip.editText?.addTextChangedListener(watcher)
        state.editText?.addTextChangedListener(watcher)
        city.editText?.addTextChangedListener(watcher)
        address.editText?.addTextChangedListener(watcher)
        addressSince.editText?.addTextChangedListener(watcher)
        birthDate.editText?.addTextChangedListener(watcher)
        email .editText?.addTextChangedListener(watcher)
        ownHome.editText?.addTextChangedListener(watcher)
        homePhone.editText?.addTextChangedListener(watcher)
        workPhone.editText?.addTextChangedListener(watcher)
        military.editText?.addTextChangedListener(watcher)
        timeToCall.editText?.addTextChangedListener(watcher)
        employer.editText?.addTextChangedListener(watcher)
        jobTitle.editText?.addTextChangedListener(watcher)
        employedMonth.editText?.addTextChangedListener(watcher)
        monthlyIncome.editText?.addTextChangedListener(watcher)
        payDate1.editText?.addTextChangedListener(watcher)
        payDate2.editText?.addTextChangedListener(watcher)
        payFrequency.editText?.addTextChangedListener(watcher)
        driversLicense.editText?.addTextChangedListener(watcher)
        driversLicenseState.editText?.addTextChangedListener(watcher)
        bankName.editText?.addTextChangedListener(watcher)
        bankPhone.editText?.addTextChangedListener(watcher)
        bankAba.editText?.addTextChangedListener(watcher)
        bankAccount.editText?.addTextChangedListener(watcher)
        bankAccountType.editText?.addTextChangedListener(watcher)
        directDeposit.editText?.addTextChangedListener(watcher)
        ssn.editText?.addTextChangedListener(watcher)
        bankAccountSince.editText?.addTextChangedListener(watcher)
        incomeType.editText?.addTextChangedListener(watcher)

        timeToCall.editText?.isFocusable = false
        payFrequency.editText?.isFocusable = false
        bankAccountType.editText?.isFocusable = false
        incomeType.editText?.isFocusable = false
        military.editText?.isFocusable = false
        ownHome.editText?.isFocusable = false
        directDeposit.editText?.isFocusable = false
        birthDate.editText?.isFocusable = false
        payDate1.editText?.isFocusable = false
        payDate2.editText?.isFocusable = false

        time_to_call.setOnClickListener { selectFromOption(timeToCallList, timeToCall.editText) }
        timeToCall.editText?.setOnClickListener { selectFromOption(timeToCallList, timeToCall.editText) }

        pay_frequency.setOnClickListener { selectFromOption(payFrequencyList, payFrequency.editText) }
        payFrequency.editText?.setOnClickListener { selectFromOption(payFrequencyList, payFrequency.editText) }

        bank_account_type.setOnClickListener { selectFromOption(bankAccountTypeList, bankAccountType.editText) }
        bankAccountType.editText?.setOnClickListener { selectFromOption(bankAccountTypeList, bankAccountType.editText) }

        income_type.setOnClickListener { selectFromOption(incomeTypeList, incomeType.editText) }
        incomeType.editText?.setOnClickListener { selectFromOption(incomeTypeList, incomeType.editText) }

        militaryInput.setOnClickListener { selectFromOption(militaryStatusList, military.editText) }
        military.editText?.setOnClickListener { selectFromOption(militaryStatusList, military.editText) }

        own_home.setOnClickListener { selectFromOption(residenceStatusList, ownHome.editText) }
        ownHome.editText?.setOnClickListener { selectFromOption(residenceStatusList, ownHome.editText) }

        direct_deposit.setOnClickListener { selectFromOption(depositStatusList, directDeposit.editText) }
        directDeposit.editText?.setOnClickListener { selectFromOption(depositStatusList, directDeposit.editText) }

        birth_date.setOnClickListener { showCustomDialog(birthDate.editText) }
        birthDate.editText?.setOnClickListener { showCustomDialog(birthDate.editText) }

        pay_date1.setOnClickListener {
            if (payDate2.editText?.text.toString() != "")
                payDate2.editText?.setText("")
            showCustomDialog(payDate1.editText)
        }
        payDate1.editText?.setOnClickListener {
            if (payDate2.editText?.text.toString() != "")
                payDate2.editText?.setText("")
            showCustomDialog(payDate1.editText)
        }

        pay_date2.setOnClickListener {
            if (payDate1Filled)
                showCustomDialog(payDate2.editText)
            else
                Toast.makeText(this, "Select Pay date 1 first", Toast.LENGTH_SHORT).show()
        }
        payDate2.editText?.setOnClickListener {
            if (payDate1Filled && payDate1.error == null)
                showCustomDialog(payDate2.editText)
            else
                Toast.makeText(this, "Select Pay date 1 first", Toast.LENGTH_SHORT).show()
        }

        rates.div_title.text = "Rates and Fees"
        terms.div_title.text = "Terms of Use"
        privacy.div_title.text = "Privacy Policy"
        econsent.div_title.text = "E-Consent"
        responsible.div_title.text = "Responsible Lending  Policy"
        marketing.div_title.text = "Marketing Practices"
        faq.div_title.text = "FAQ"
        disclaimer.div_title.text = "Disclaimer and APR Representative "

        if (intent.getStringExtra("btn") == "installment"){
            rates.info.text = getTextFromFile("ratesinstallment.txt")
            terms.info.text = getTextFromFile("termsinstallment.txt")
            privacy.info.text = getTextFromFile("privacyinstallment.txt")
            econsent.info.text = getTextFromFile("econsentinstallment.txt")
            responsible.info.text = getTextFromFile("responsibleinstallment.txt")
            marketing.info.text = getTextFromFile("marketinginstallment.txt")
            faq.info.text = getTextFromFile("faqinstallment.txt")
            disclaimer.info.text = getTextFromFile("disclaimerinstallment.txt")
        }else if (intent.getStringExtra("btn") == "payday"){
            rates.info.text = getTextFromFile("rates.txt")
            terms.info.text = getTextFromFile("terms.txt")
            privacy.info.text = getTextFromFile("privacy.txt")
            econsent.info.text = getTextFromFile("econsent.txt")
            responsible.info.text = getTextFromFile("responsible.txt")
            marketing.info.text = getTextFromFile("marketing.txt")
            faq.info.text = getTextFromFile("faq.txt")
            disclaimer.info.text = getTextFromFile("disclaimer.txt")
        }

        rates.setOnClickListener { toggleLayout(rates) }
        terms.setOnClickListener { toggleLayout(terms) }
        privacy.setOnClickListener { toggleLayout(privacy) }
        econsent.setOnClickListener { toggleLayout(econsent) }
        responsible.setOnClickListener { toggleLayout(responsible) }
        marketing.setOnClickListener { toggleLayout(marketing) }
        faq.setOnClickListener { toggleLayout(faq) }
        disclaimer.setOnClickListener { toggleLayout(disclaimer) }

//        fillFields()
    }

    private fun fillFields(){
        if (BuildConfig.DEBUG){
            requestedAmount.editText?.setText("500")
            firstName.editText?.setText("Will")
            lastName.editText?.setText("Smith")
            zip.editText?.setText("12288")
            state.editText?.setText("CA")
            city.editText?.setText("Los Angeles")
            address.editText?.setText("123 Test address")
            addressSince.editText?.setText("12")
            birthDate.editText?.setText("1980-06-22")
            email.editText?.setText("test@example.com")
            homePhone.editText?.setText("2142288070")
            workPhone.editText?.setText("4102477840")
            timeToCall.editText?.setText("ANYTIME")
            employer.editText?.setText("Test")
            jobTitle.editText?.setText("Test")
            employedMonth.editText?.setText("3")
            monthlyIncome.editText?.setText("2500")
            payDate1.editText?.setText("2019-03-28")
            payDate2.editText?.setText("2019-03-30")
            payFrequency.editText?.setText("WEEKLY")
            driversLicense.editText?.setText("A1234567")
            driversLicenseState.editText?.setText("CA")
            bankName.editText?.setText("JPMORGAN CHASE BANK")
            bankPhone.editText?.setText("8004460135")
            bankAba.editText?.setText("101089742")
            bankAccount.editText?.setText("147565")
            bankAccountType.editText?.setText("CHECKING")
            ssn.editText?.setText("123456789")
            bankAccountSince.editText?.setText("12")
            incomeType.editText?.setText("EMPLOYMENT")
        }
    }

    fun getTextFromFile(path: String): String {

        var reader: BufferedReader? = null

        try {
            reader = BufferedReader(
                    InputStreamReader(assets.open(path)));

            text = reader.readLines().joinToString("\n")

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    //log the exception
                }
            }
        }
        return text
    }

    private fun toggleLayout(view: View){
        view.div_content.toggle()
        if (view.div_content.isExpanded){
            view.div_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up, 0)
        }else{
            view.div_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down, 0)
        }
    }

    fun selectFromOption(list: ArrayList<String>, et: EditText?){
        showCustomDialog(list, et)
        et?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                dialog?.cancel()
            }
        })
    }

    private fun showCustomDialog(list: ArrayList<String>, editField: EditText?) {
        val builder = AlertDialog.Builder(this)
        val layoutInflater = layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        val rv = dialogView.findViewById<RecyclerView>(R.id.rv)
        adapter = DialogListAdapter(this, list, editField)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        builder.setView(dialogView)

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        dialog = builder.create()
        dialog?.setOnShowListener {
            this.let {
                ctx -> ContextCompat.getColor(ctx, R.color.red)
            }.let {
                color -> dialog?.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(color)
            }
        }
        dialog?.show()
    }

    private fun showCustomDialog(editField: EditText?){
        dialog?.dismiss()
        val builder = AlertDialog.Builder(this)
        val layoutInflater = layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.date_picker_dialog, null)
        builder.setView(dialogView)
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel() }
        builder.setPositiveButton("OK") { dialog, _ ->
            val datePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)
            val date = "${datePicker.year}-${String.format("%02d", datePicker.month+1)}-${String.format("%02d", datePicker.dayOfMonth)}"
            editField?.setText(date)
            dialog.cancel() }
        dialog = builder.create()
        dialog?.setOnShowListener {
            this.let {
                ctx -> ContextCompat.getColor(ctx, R.color.black) }?.let {
                color -> dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(color)
            }
            this.let {
                ctx -> ContextCompat.getColor(ctx, R.color.red) }?.let {
                color -> dialog?.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(color)
            }
        }
        dialog?.show()
    }

    fun enableButton(){
        if (requestedAmountFilled && firstNameFilled && lastNameFilled && zipFilled && stateFilled &&
                cityFilled && addressFilled && addressSinceFilled && birthDateFilled && emailFilled &&
                ownHomeFilled && homePhoneFilled && workPhoneFilled && militaryFilled && timeToCallFilled &&
                employerFilled && jobTitleFilled && employedMonthFilled && monthlyIncomeFilled &&
                payDate1Filled && payDate2Filled && payFrequencyFilled && driversLicenseFilled &&
                driversLicenseStateFilled && bankNameFilled && bankPhoneFilled && bankAbaFilled &&
                bankAccountFilled && bankAccountTypeFilled && directDepositFilled && ssnFilled &&
                bankAccountSinceFilled && incomeTypeFilled){
            next.isEnabled = true
            next.setBackgroundColor(resources.getColor(R.color.main))
        }else{
            next.isEnabled = false
            next.setBackgroundColor(resources.getColor(R.color.gray))
        }
    }

    fun sendData(){

        residence = ownHome.editText?.text.toString()
        val rArr = resources.getStringArray(R.array.residence_status)
        when (residence) {
            rArr[0] -> residence = "1"
            rArr[1] -> residence = "0"
        }

        militaryStatus = military.editText?.text.toString()
        val mArr = resources.getStringArray(R.array.military_status)
        when (militaryStatus) {
            mArr[0] -> militaryStatus = "1"
            mArr[1] -> militaryStatus = "0"
        }

        depositStatus = directDeposit.editText?.text.toString()
        val dArr = resources.getStringArray(R.array.deposit_status)
        when (depositStatus) {
            dArr[0] -> depositStatus = "1"
            dArr[1] -> depositStatus = "0"
        }
        if (isOnline()){
            Thread(Runnable {
                runOnUiThread {
                    showProgressDialog("Sending...")
                }
                    lateinit var response: Response<CustomResponse>
                    if (payday){
                        try {
                            response = APICaller.getPayDayApi().createLoan("application/json",
                                    requestedAmount.editText?.text!!.toString(), employer.editText?.text!!.toString(),
                                    jobTitle.editText?.text!!.toString(), employedMonth.editText?.text!!.toString(),
                                    monthlyIncome.editText?.text!!.toString(), payDate1.editText?.text!!.toString(),
                                    payDate2.editText?.text!!.toString(), payFrequency.editText?.text!!.toString(),
                                    driversLicense.editText?.text!!.toString(), driversLicenseState.editText?.text!!.toString(),
                                    bankName.editText?.text!!.toString(), numberClearMask(bankPhone.editText?.text!!.toString()),
                                    bankAba.editText?.text!!.toString(), bankAccount.editText?.text!!.toString(),
                                    bankAccountType.editText?.text!!.toString(), depositStatus,
                                    firstName.editText?.text!!.toString(), lastName.editText?.text!!.toString(),
                                    ssn.editText?.text!!.toString(), birthDate.editText?.text!!.toString(),
                                    residence, address.editText?.text!!.toString(),
                                    city.editText?.text!!.toString(), state.editText?.text!!.toString(),
                                    zip.editText?.text!!.toString(), email.editText?.text!!.toString(),
                                    numberClearMask(homePhone.editText?.text!!.toString()), numberClearMask(workPhone.editText?.text!!.toString()),
                                    timeToCall.editText?.text!!.toString(), militaryStatus,
                                    addressSince.editText?.text!!.toString(), bankAccountSince.editText?.text!!.toString(),
                                    incomeType.editText?.text!!.toString(), session_id, " ", BuildConfig.DOMAIN,
                                    BuildConfig.USER_AGENT, client_ip_address, BuildConfig.AFFILIATE_ID,
                                    BuildConfig.API_KEY, tier_key).execute()
                        }catch (e: SocketTimeoutException){
                            dialog?.dismiss()
                            Toast.makeText(baseContext, "Server error, try again later", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }catch (e: IOException){
                            dialog?.dismiss()
                            Toast.makeText(baseContext, "Server error, try again later", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }else{
                        try {
                            response = APICaller.getInstallmentApi().createLoan("application/json",
                                    requestedAmount.editText?.text!!.toString(), employer.editText?.text!!.toString(),
                                    jobTitle.editText?.text!!.toString(), employedMonth.editText?.text!!.toString(),
                                    monthlyIncome.editText?.text!!.toString(), payDate1.editText?.text!!.toString(),
                                    payDate2.editText?.text!!.toString(), payFrequency.editText?.text!!.toString(),
                                    driversLicense.editText?.text!!.toString(), driversLicenseState.editText?.text!!.toString(),
                                    bankName.editText?.text!!.toString(), numberClearMask(bankPhone.editText?.text!!.toString()),
                                    bankAba.editText?.text!!.toString(), bankAccount.editText?.text!!.toString(),
                                    bankAccountType.editText?.text!!.toString(), depositStatus,
                                    firstName.editText?.text!!.toString(), lastName.editText?.text!!.toString(),
                                    ssn.editText?.text!!.toString(), birthDate.editText?.text!!.toString(),
                                    residence, address.editText?.text!!.toString(),
                                    city.editText?.text!!.toString(), state.editText?.text!!.toString(),
                                    zip.editText?.text!!.toString(), email.editText?.text!!.toString(),
                                    numberClearMask(homePhone.editText?.text!!.toString()), numberClearMask(workPhone.editText?.text!!.toString()),
                                    timeToCall.editText?.text!!.toString(), militaryStatus,
                                    addressSince.editText?.text!!.toString(), bankAccountSince.editText?.text!!.toString(),
                                    incomeType.editText?.text!!.toString(), session_id, " ", BuildConfig.DOMAIN,
                                    BuildConfig.USER_AGENT, client_ip_address, BuildConfig.AFFILIATE_ID,
                                    BuildConfig.API_KEY, tier_key).execute()
                        }catch (e: SocketTimeoutException){
                            dialog?.dismiss()
                            Toast.makeText(baseContext, "Server error, try again later", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }catch (e: IOException){
                            dialog?.dismiss()
                            Toast.makeText(baseContext, "Server error, try again later", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if (responseBody!!.status == "error"){
                            if (responseBody.content != null){
                                if (responseBody.content!!.first_name != null){
                                    gotoErrorField(firstName, responseBody.content!!.first_name?.get(0))
                                }
                                if (responseBody.content!!.last_name != null){
                                    gotoErrorField(lastName, responseBody.content!!.last_name?.get(0))
                                }
                                if (responseBody.content!!.zip != null){
                                    gotoErrorField(zip, responseBody.content!!.zip?.get(0))
                                }
                                if (responseBody.content!!.state != null){
                                    gotoErrorField(state, responseBody.content!!.state?.get(0))
                                }
                                if (responseBody.content!!.address != null){
                                    gotoErrorField(address, responseBody.content!!.address?.get(0))
                                }
                                if (responseBody.content!!.address_length_months != null){
                                    gotoErrorField(addressSince, responseBody.content!!.address_length_months?.get(0))
                                }
                                if (responseBody.content!!.birth_date != null){
                                    gotoErrorField(birthDate, responseBody.content!!.birth_date?.get(0))
                                }
                                if (responseBody.content!!.email != null){
                                    gotoErrorField(email, responseBody.content!!.email?.get(0))
                                }
                                if (responseBody.content!!.home_phone != null){
                                    gotoErrorField(homePhone, responseBody.content!!.home_phone?.get(0))
                                }
                                if (responseBody.content!!.work_phone != null){
                                    gotoErrorField(workPhone, responseBody.content!!.work_phone?.get(0))
                                }
                                if (responseBody.content!!.employer != null){
                                    gotoErrorField(employer, responseBody.content!!.employer?.get(0))
                                }
                                if (responseBody.content!!.job_title != null){
                                    gotoErrorField(jobTitle, responseBody.content!!.job_title?.get(0))
                                }
                                if (responseBody.content!!.employed_months != null){
                                    gotoErrorField(employedMonth, responseBody.content!!.employed_months?.get(0))
                                }
                                if (responseBody.content!!.monthly_income != null){
                                    gotoErrorField(monthlyIncome, responseBody.content!!.monthly_income?.get(0))
                                }
                                if (responseBody.content!!.pay_date1 != null){
                                    gotoErrorField(payDate1, responseBody.content!!.pay_date1?.get(0))
                                }
                                if (responseBody.content!!.pay_date2 != null){
                                    gotoErrorField(payDate2, responseBody.content!!.pay_date2?.get(0))
                                }
                                if (responseBody.content!!.drivers_license_number != null){
                                    gotoErrorField(driversLicense, responseBody.content!!.drivers_license_number?.get(0))
                                }
                                if (responseBody.content!!.drivers_license_state != null){
                                    gotoErrorField(driversLicenseState, responseBody.content!!.drivers_license_state?.get(0))
                                }
                                if (responseBody.content!!.bank_name != null){
                                    gotoErrorField(bankName, responseBody.content!!.bank_name?.get(0))
                                }
                                if (responseBody.content!!.bank_phone != null){
                                    gotoErrorField(bankPhone, responseBody.content!!.bank_phone?.get(0))
                                }
                                if (responseBody.content!!.bank_aba != null){
                                    gotoErrorField(bankAba, responseBody.content!!.bank_aba?.get(0))
                                }
                                if (responseBody.content!!.bank_account_number != null){
                                    gotoErrorField(bankAccount, responseBody.content!!.bank_account_number?.get(0))
                                }
                                if (responseBody.content!!.ssn != null){
                                    gotoErrorField(ssn, responseBody.content!!.ssn?.get(0))
                                }
                                if (responseBody.content!!.bank_account_length_months != null){
                                    gotoErrorField(bankAccountSince, responseBody.content!!.bank_account_length_months?.get(0))
                                }
                            }

                            runOnUiThread {
                                Answers.getInstance().logCustom(CustomEvent("SEND_ERROR $response"))
                                dialog?.dismiss()
                            }
                        }else if (responseBody.status == "success"){
                            runOnUiThread {
                                dialog?.dismiss()
                                Toast.makeText(baseContext, "Success", Toast.LENGTH_LONG).show()
                            }
                        } else if (responseBody.status == "reject"){
                            runOnUiThread {
                                dialog?.dismiss()
                                Toast.makeText(baseContext, "Loan rejected", Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        runOnUiThread {
                            dialog?.dismiss()
                            Toast.makeText(baseContext, "Server error, try again later", Toast.LENGTH_LONG).show()
                        }
                    }
            }).start()
        }else{
            val builder1 = AlertDialog.Builder(this)
            builder1.setMessage("Turn on your Internet connection in order to continue")
            builder1.setCancelable(true)

            builder1.setPositiveButton(
                    "Ok"
            ) { dialog, id -> dialog.cancel() }

            val alert11 = builder1.create()

            alert11?.setOnShowListener {
                this.let {
                    ctx -> ContextCompat.getColor(ctx, R.color.black)
                }.let {
                    color -> alert11.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(color)
                }
            }

            alert11.show()
        }
    }

    fun gotoErrorField(v: TextInputLayout, msg: String?){
        when (v){
            payDate1 -> {
                runOnUiThread {
                    scroll.scrollTo(0, v.bottom)
                    v.error = msg?.replace("_", " ")
                    v.editText?.error = "This field did not pass the validation. Please check it!!!"
                    Toast.makeText(this, "Choose another date for payment", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                runOnUiThread {
                    scroll.fullScroll(ScrollView.FOCUS_UP)
                    v.error = msg?.replace("_", " ")
                    v.editText?.error = "This field did not pass the validation. Please check it!!!"
                    Toast.makeText(this, "${msg?.replace("_", " ")}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting
    }

    @SuppressLint("ResourceType")
    fun showProgressDialog(message: String){
        if(dialog!=null){
            dialog?.dismiss()
        }
        val builder = AlertDialog.Builder(this)
        val layoutInflater = layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.progress_bar_dialog, null)
        val messageView = dialogView.findViewById<TextView>(R.id.message)
        messageView.text = message
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.show()
        dialog?.window?.setLayout(600, 275)
    }

    fun checkFields(layout: TextInputLayout){
        when(layout){
            requestedAmount -> {
                if (requestedAmount.editText?.text.toString() != ""){
                    if (requestedAmount.editText!!.text.toString().toInt() < min ||
                            requestedAmount.editText!!.text.toString().toInt() > max){
                        requestedAmount.boxStrokeColor = resources.getColor(R.color.red)
                        requestedAmount.editText?.error = "Range error " + min + "-" + max
                        requestedAmount.error = "Range error " + min + "-" + max
                    }else{
                        requestedAmount.boxStrokeColor = resources.getColor(R.color.black)
                        requestedAmount.editText?.error = null
                        requestedAmount.error = null
                    }
                }else{
                    requestedAmount.editText?.error = "Field must not be empty"
                    requestedAmount.error = "Field must not be empty"
                    requestedAmountFilled = false
                }
            }
            firstName -> {
                if (firstName.editText?.text.toString() != ""){
                    if (!firstName.editText!!.text.toString().matches("[a-zA-Z]+".toRegex())){
                        firstName.boxStrokeColor = resources.getColor(R.color.red)
                        firstName.editText?.error = "Field should have only letters"
                        firstName.error = "Field should have only letters"
                    }else{
                        firstName.boxStrokeColor = resources.getColor(R.color.black)
                        firstName.editText?.error = null
                        firstName.error = null
                    }
                }else{
                    firstName.editText?.error = "Field must not be empty"
                    firstName.error = "Field must not be empty"
                    firstNameFilled = false
                }
            }
            lastName -> {
                if (lastName.editText?.text.toString() != ""){
                    if (!lastName.editText!!.text.toString().matches("[a-zA-Z]+".toRegex())){
                        lastName.boxStrokeColor = resources.getColor(R.color.red)
                        lastName.editText?.error = "Field should have only letters"
                        lastName.error = "Field should have only letters"
                    }else{
                        lastName.boxStrokeColor = resources.getColor(R.color.black)
                        lastName.editText?.error = null
                        lastName.error = null
                    }
                }else{
                    lastName.editText?.error = "Field must not be empty"
                    lastName.error = "Field must not be empty"
                    lastNameFilled = false
                }
            }
            zip -> {
                if (zip.editText?.text.toString() != ""){
                    if (zip.editText?.text.toString().length != 5){
                        zip.editText?.error = "Zip code should consist of 5 numbers"
                        zip.error = "Zip code should consist of 5 numbers"
                    }else{
                        zip.boxStrokeColor = resources.getColor(R.color.black)
                        zip.editText?.error = null
                        zip.error = null
                    }
                }
            }
            state -> {
                if (state.editText?.text.toString() != ""){
                    if (!state.editText!!.text.toString().matches("[A-Z]+".toRegex())){
                        state.boxStrokeColor = resources.getColor(R.color.red)
                        state.editText?.error = "Field must contain only capital letters"
                        state.error = "Field must contain only capital letters"
                    }else{
                        state.boxStrokeColor = resources.getColor(R.color.black)
                        state.editText?.error = null
                        state.error = null
                    }
                }else{
                    state.editText?.error = "Field must not be empty"
                    state.error = "Field must not be empty"
                    stateFilled = false
                }
            }
            city -> {
                if (city.editText?.text.toString() != ""){
                    if (city.error != null){
                        city.error = null
                    }
                }else{
                    city.editText?.error = "Field must not be empty"
                    city.error = "Field must not be empty"
                    cityFilled = false
                }
            }
            address -> {
                if (address.editText?.text.toString() != ""){
                    if (address.editText!!.text.toString().length < 2){
                        address.boxStrokeColor = resources.getColor(R.color.red)
                        address.editText?.error = "Invalid address"
                        address.error = "Invalid address"
                    }else{
                        address.boxStrokeColor = resources.getColor(R.color.black)
                        address.editText?.error = null
                        address.error = null
                    }
                }else{
                    address.editText?.error = "Field must not be empty"
                    address.error = "Field must not be empty"
                    addressFilled = false
                }
            }
            addressSince -> {
                if (addressSince.editText?.text.toString() != ""){
                    if (addressSince.editText!!.text.toString().toInt() < 1 ||
                            addressSince.editText!!.text.toString().toInt() > 120){
                        addressSince.boxStrokeColor = resources.getColor(R.color.red)
                        addressSince.editText?.error = "Invalid address length"
                        addressSince.error = "Invalid address length"
                    }else{
                        addressSince.boxStrokeColor = resources.getColor(R.color.black)
                        addressSince.editText?.error = null
                        addressSince.error = null
                    }
                }else{
                    addressSince.editText?.error = "Field must not be empty"
                    addressSince.error = "Field must not be empty"
                    addressSinceFilled = false
                }
            }
            email -> {
                if (email.editText?.text.toString() != ""){
                    if (!email.editText!!.text.toString().contains("@")){
                        email.boxStrokeColor = resources.getColor(R.color.red)
                        email.editText?.error = "Invalid email"
                        email.error = "Invalid email"
                    }else{
                        email.boxStrokeColor = resources.getColor(R.color.black)
                        email.editText?.error = null
                        email.error = null
                    }
                }else{
                    email.editText?.error = "Field must not be empty"
                    email.error = "Field must not be empty"
                    emailFilled = false
                }
            }
            employer -> {
                if (employer.editText?.text.toString() != ""){
                    if (employer.editText!!.text.toString().length < 2 ||
                            employer.editText!!.text.toString().length > 128){
                        employer.boxStrokeColor = resources.getColor(R.color.red)
                        employer.editText?.error = "Invalid employer"
                        employer.error = "Invalid employer"
                    }else{
                        employer.boxStrokeColor = resources.getColor(R.color.black)
                        employer.editText?.error = null
                        employer.error = null
                    }
                }else{
                    employer.editText?.error = "Field must not be empty"
                    employer.error = "Field must not be empty"
                    employerFilled = false
                }
            }
            jobTitle -> {
                if (jobTitle.editText?.text.toString() != ""){
                    if (jobTitle.editText!!.text.toString().length < 2 ||
                            jobTitle.editText!!.text.toString().length > 128){
                        jobTitle.boxStrokeColor = resources.getColor(R.color.red)
                        jobTitle.editText?.error = "Invalid job title"
                        jobTitle.error = "Invalid job title"
                    }else{
                        jobTitle.boxStrokeColor = resources.getColor(R.color.black)
                        jobTitle.editText?.error = null
                        jobTitle.error = null
                    }
                }else{
                    jobTitle.editText?.error = "Field must not be empty"
                    jobTitle.error = "Field must not be empty"
                    jobTitleFilled = false
                }
            }
            employedMonth -> {
                if (employedMonth.editText?.text.toString() != ""){
                    if (employedMonth.editText!!.text.toString().toInt() < 1 ||
                            employedMonth.editText!!.text.toString().toInt() > 60){
                        employedMonth.boxStrokeColor = resources.getColor(R.color.red)
                        employedMonth.editText?.error = "Invalid employed range"
                        employedMonth.error = "Invalid employed range"
                    }else{
                        employedMonth.boxStrokeColor = resources.getColor(R.color.black)
                        employedMonth.editText?.error = null
                        employedMonth.error = null
                    }
                }else{
                    employedMonth.editText?.error = "Field must not be empty"
                    employedMonth.error = "Field must not be empty"
                    employedMonthFilled = false
                }
            }
            monthlyIncome -> {
                if (monthlyIncome.editText?.text.toString() != ""){
                    if (monthlyIncome.editText!!.text.toString().toInt() < 1126 ||
                            monthlyIncome.editText!!.text.toString().toInt() > 6000){
                        monthlyIncome.boxStrokeColor = resources.getColor(R.color.red)
                        monthlyIncome.editText?.error = "Invalid income"
                        monthlyIncome.error = "Invalid income"
                    }else{
                        monthlyIncome.boxStrokeColor = resources.getColor(R.color.black)
                        monthlyIncome.editText?.error = null
                        monthlyIncome.error = null
                    }
                }else{
                    monthlyIncome.editText?.error = "Field must not be empty"
                    monthlyIncome.error = "Field must not be empty"
                    monthlyIncomeFilled = false
                }
            }
            driversLicenseState -> {
                if (driversLicenseState.editText?.text.toString() != ""){
                    if (!driversLicenseState.editText!!.text.toString().matches("[A-Z]+".toRegex())){
                        driversLicenseState.boxStrokeColor = resources.getColor(R.color.red)
                        driversLicenseState.editText?.error = "Field must contain only capital letters"
                        driversLicenseState.error = "Field must contain only capital letters"
                    }else{
                        driversLicenseState.boxStrokeColor = resources.getColor(R.color.black)
                        driversLicenseState.editText?.error = null
                        driversLicenseState.error = null
                    }
                }else{
                    driversLicenseState.editText?.error = "Field must not be empty"
                    driversLicenseState.error = "Field must not be empty"
                    driversLicenseStateFilled = false
                }
            }
            bankName -> {
                if (bankName.editText?.text.toString() != ""){
                    if (bankName.editText!!.text.toString().length < 2 ||
                            bankName.editText!!.text.toString().length > 128){
                        bankName.boxStrokeColor = resources.getColor(R.color.red)
                        bankName.editText?.error = "Invalid bank name"
                        bankName.error = "Invalid bank name"
                    }else{
                        bankName.boxStrokeColor = resources.getColor(R.color.black)
                        bankName.editText?.error = null
                        bankName.error = null
                    }
                }else{
                    bankName.editText?.error = "Field must not be empty"
                    bankName.error = "Field must not be empty"
                    bankNameFilled = false
                }
            }
            bankAba -> {
                if (bankAba.editText?.text.toString() != ""){
                    if (bankAba.editText!!.text.toString().length != 9){
                        bankAba.boxStrokeColor = resources.getColor(R.color.red)
                        bankAba.editText?.error = "Field must contain 9 numbers"
                        bankAba.error = "Field must contain 9 numbers"
                    }else{
                        bankAba.boxStrokeColor = resources.getColor(R.color.black)
                        bankAba.editText?.error = null
                        bankAba.error = null
                    }
                }else{
                    bankAba.editText?.error = "Field must not be empty"
                    bankAba.error = "Field must not be empty"
                    bankAbaFilled = false
                }
            }
            bankAccount -> {
                if (bankAccount.editText?.text.toString() != ""){
                    if (bankAccount.editText!!.text.toString().length < 4 ||
                            bankAccount.editText!!.text.toString().length > 30){
                        bankAccount.boxStrokeColor = resources.getColor(R.color.red)
                        bankAccount.editText?.error = "Invalid bank account"
                        bankAccount.error = "Invalid bank account"
                    }else{
                        bankAccount.boxStrokeColor = resources.getColor(R.color.black)
                        bankAccount.editText?.error = null
                        bankAccount.error = null
                    }
                }else{
                    bankAccount.editText?.error = "Field must not be empty"
                    bankAccount.error = "Field must not be empty"
                    bankAccountFilled = false
                }
            }
            ssn -> {
                if (ssn.editText?.text.toString() != ""){
                    if (ssn.editText!!.text.toString().length != 9){
                        ssn.boxStrokeColor = resources.getColor(R.color.red)
                        ssn.editText?.error = "Field must contain 9 numbers"
                        ssn.error = "Field must contain 9 numbers"
                    }else{
                        ssn.boxStrokeColor = resources.getColor(R.color.black)
                        ssn.editText?.error = null
                        ssn.error = null
                    }
                }else{
                    ssn.editText?.error = "Field must not be empty"
                    ssn.error = "Field must not be empty"
                    ssnFilled = false
                }
            }
            bankAccountSince -> {
                if (bankAccountSince.editText?.text.toString() != ""){
                    if (bankAccountSince.editText!!.text.toString().toInt() < 1 ||
                            bankAccountSince.editText!!.text.toString().toInt() > 30){
                        bankAccountSince.boxStrokeColor = resources.getColor(R.color.red)
                        bankAccountSince.editText?.error = "Invalid bank account length"
                        bankAccountSince.error = "Invalid bank account length"
                    }else{
                        bankAccountSince.boxStrokeColor = resources.getColor(R.color.black)
                        bankAccountSince.editText?.error = null
                        bankAccountSince.error = null
                    }
                }else{
                    bankAccountSince.editText?.error = "Field must not be empty"
                    bankAccountSince.error = "Field must not be empty"
                    bankAccountSinceFilled = false
                }
            }
            birthDate -> {
                val cal = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                cal.time = sdf.parse(birthDate.editText?.text.toString())
                val today = Calendar.getInstance()
                var age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR)

                if (today.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR)) {
                    age--
                }
                if (age < 18 || age > 95){
                    birthDate.boxStrokeColor = resources.getColor(R.color.red)
                    birthDate.error = "Your age should be greater than 18 and less than 95"
                    Toast.makeText(this, "Your age should be greater than 18 and less than 95", Toast.LENGTH_LONG).show()
                }else{
                    birthDate.boxStrokeColor = resources.getColor(R.color.black)
                    birthDate.editText?.error = null
                    birthDate.error = null
                }
            }
            payDate1 -> {
                if (payDate1.editText?.text.toString() != ""){
                    val cal = Calendar.getInstance()
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    cal.time = sdf.parse(payDate1.editText?.text.toString())
                    val today = Calendar.getInstance()
                    if (!cal.after(today)){
                        payDate1.boxStrokeColor = resources.getColor(R.color.red)
                        payDate1.error = "Pay date 1 should be after today's date"
                        Toast.makeText(this, "Pay date 1 should be after today's date", Toast.LENGTH_LONG).show()
                    }else{
                        payDate1.boxStrokeColor = resources.getColor(R.color.black)
                        payDate1.editText?.error = null
                        payDate1.error = null
                    }
                }
            }
            payDate2 -> {
                if (payDate2.editText?.text.toString() != ""){
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val date2 = Calendar.getInstance()
                    date2.time = sdf.parse(payDate2.editText?.text.toString())
                    val date1 = Calendar.getInstance()
                    date1.time = sdf.parse(payDate1.editText?.text.toString())
                    if (!date2.after(date1)){
                        payDate2.boxStrokeColor = resources.getColor(R.color.red)
                        payDate2.error = "Pay date 2 should be after Pay date 1"
                        Toast.makeText(this, "Pay date 2 should be after Pay date 1", Toast.LENGTH_LONG).show()
                    }else{
                        payDate2.boxStrokeColor = resources.getColor(R.color.black)
                        payDate2.editText?.error = null
                        payDate2.error = null
                    }
                }
            }
        }
    }

    private inner class SingleWatcher : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, after: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, before: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when {
                isEqual(requestedAmount, editable) -> {
                    requestedAmountFilled = true
                    checkFields(requestedAmount)
                }
                isEqual(firstName, editable) -> {
                    firstNameFilled = true
                    checkFields(firstName)
                }
                isEqual(lastName, editable) -> {
                    lastNameFilled = true
                    checkFields(lastName)
                }
                isEqual(zip, editable) -> {
                    zipFilled = true
                    checkFields(zip)
                }
                isEqual(state, editable) -> {
                    stateFilled = true
                    checkFields(state)
                }
                isEqual(city, editable) -> {
                    cityFilled = true
                    checkFields(city)
                }
                isEqual(address, editable)  -> {
                    addressFilled = true
                    checkFields(address)
                }
                isEqual(addressSince, editable) -> {
                    addressSinceFilled = true
                    checkFields(addressSince)
                }
                isEqual(birthDate, editable) -> {
                    birthDateFilled = true
                    checkFields(birthDate)
                }
                isEqual(email, editable) -> {
                    emailFilled = true
                    checkFields(email)
                }
                isEqual(ownHome, editable) -> {
                    ownHomeFilled = true
                }
                isEqual(homePhone, editable) -> {
                    homePhoneFilled = true
                    checkFields(homePhone)
                }
                isEqual(workPhone, editable) -> {
                    workPhoneFilled = true
                    checkFields(workPhone)
                }
                isEqual(military, editable) -> {
                    militaryFilled = true
                }
                isEqual(timeToCall, editable) -> {
                    timeToCallFilled = true
                }
                isEqual(employer, editable) -> {
                    employerFilled = true
                    checkFields(employer)
                }
                isEqual(jobTitle, editable) -> {
                    jobTitleFilled = true
                    checkFields(jobTitle)
                }
                isEqual(employedMonth, editable) -> {
                    employedMonthFilled = true
                    checkFields(employedMonth)
                }
                isEqual(monthlyIncome, editable) -> {
                    monthlyIncomeFilled = true
                    checkFields(monthlyIncome)
                }
                isEqual(payDate1, editable) -> {
                    payDate1Filled = true
                    checkFields(payDate1)
                }
                isEqual(payDate2, editable) -> {
                    payDate2Filled = true
                    checkFields(payDate2)
                }
                isEqual(payFrequency, editable) -> {
                    payFrequencyFilled = true
                    checkFields(payFrequency)
                }
                isEqual(driversLicense, editable) -> {
                    driversLicenseFilled = true
                    checkFields(driversLicense)
                }
                isEqual(driversLicenseState, editable) -> {
                    driversLicenseStateFilled = true
                    checkFields(driversLicenseState)
                }
                isEqual(bankName, editable) -> {
                    bankNameFilled = true
                    checkFields(bankName)
                }
                isEqual(bankPhone, editable) -> {
                    bankPhoneFilled = true
                    checkFields(bankPhone)
                }
                isEqual(bankAba, editable) -> {
                    bankAbaFilled = true
                    checkFields(bankAba)
                }
                isEqual(bankAccount, editable) -> {
                    bankAccountFilled = true
                    checkFields(bankAccount)
                }
                isEqual(bankAccountType, editable) -> {
                    bankAccountTypeFilled = true
                }
                isEqual(directDeposit, editable) -> {
                    directDepositFilled = true
                }
                isEqual(ssn, editable) -> {
                    ssnFilled = true
                    checkFields(ssn)
                }
                isEqual(bankAccountSince, editable) -> {
                    bankAccountSinceFilled = true
                    checkFields(bankAccountSince)
                }
                isEqual(incomeType, editable) -> {
                    incomeTypeFilled = true
                }
            }
            enableButton()
        }

        private fun isEqual(input: TextInputLayout, value: Editable): Boolean {
            return input.editText?.text?.hashCode() == value.hashCode()
        }
    }

    fun createMaxLengthFilter(maxLength : Int): Array<InputFilter> =
            arrayOf(InputFilter.LengthFilter(maxLength))

    protected fun numberClearMask(number : String) : String{
        val result : MutableList<Char> = ArrayList()
        val list = number.toList()
        for (element in list){
            if(element != '+' && element != ' ' && element != '('
                    && element != ')' && element != '-') {
                result.add(element)
            }
        }
        return result.joinToString("")
    }
}
