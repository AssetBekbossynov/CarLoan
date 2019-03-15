package kz.assetbekbossynov.payday

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import android.text.format.Formatter.formatIpAddress
import android.net.wifi.WifiManager
import android.text.*
import android.text.format.Formatter
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.toolbar.*
import android.view.WindowManager


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
    internal var mSplashDialog: Dialog? = null
    internal var adapter: DialogListAdapter? = null

    var session_id: String = "f6ove66srpslo9o08ii6mko6h5"
    var referrer: String = "test"
    var domain: String = "example.com"
    var user_agent: String = "Mozilla/5.0+%28Windows+NT+6.1;+WOW64;+rv:26.0%29+Gecko/20100101+Firefox/26.0"
    var client_ip_address: String = "66.87.144.180"
    var affiliate_id: String = "5381"
    var api_key: String = "2f3d359d64ebdcbcb0a4d406ae79417bA5381"
    var tier_key: String = "0b54423d79_1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_fields)

        titleLayout.text = "Questionnaire"

        backbutton.setOnClickListener {
            onBackPressed()
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

        next.setOnClickListener {
            if(next.isEnabled){
                sendData()
            }else{
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
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
        addressSince.hint = "Address about(month)"
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
        directDeposit.hint = "Direct deposit?"
        ssn.hint = "Social security number"
        bankAccountSince.hint = "Bank account since"
        incomeType.hint = "Income type"

        requestedAmount.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        firstName.editText?.inputType = InputType.TYPE_CLASS_TEXT and InputType.TYPE_TEXT_FLAG_MULTI_LINE.inv() or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        lastName.editText?.inputType = InputType.TYPE_CLASS_TEXT and InputType.TYPE_TEXT_FLAG_MULTI_LINE.inv() or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        zip.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        addressSince.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        state.editText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        email.editText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        homePhone.editText?.inputType = InputType.TYPE_CLASS_PHONE
        workPhone.editText?.inputType = InputType.TYPE_CLASS_PHONE
        employedMonth.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        monthlyIncome.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        driversLicenseState.editText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        bankPhone.editText?.inputType = InputType.TYPE_CLASS_PHONE

        firstName.editText?.filters = createMaxLengthFilter(128)
        lastName.editText?.filters = createMaxLengthFilter(128)
        zip.editText?.filters = createMaxLengthFilter(5)
        state.editText?.filters = createMaxLengthFilter(2)
        driversLicenseState.editText?.filters = createMaxLengthFilter(2)
        bankAba.editText?.filters = createMaxLengthFilter(9)
        addressSince.editText?.filters = createMaxLengthFilter(3)
        employedMonth.editText?.filters = createMaxLengthFilter(2)

        birthDate.editText?.let {
            it.addTextChangedListener(MaskedTextChangedListener(
                    "[0000]-[00]-[00]",
                    true,
                    it, null, null
            ))
        }

        payDate1.editText?.let {
            it.addTextChangedListener(MaskedTextChangedListener(
                    "[0000]-[00]-[00]",
                    true,
                    it, null, null
            ))
        }

        payDate2.editText?.let {
            it.addTextChangedListener(MaskedTextChangedListener(
                    "[0000]-[00]-[00]",
                    true,
                    it, null, null
            ))
        }

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

        time_to_call.setOnClickListener { selectTimeToCall() }
        timeToCall.editText?.setOnClickListener { selectTimeToCall() }

        pay_frequency.setOnClickListener { selectPayFrequency() }
        payFrequency.editText?.setOnClickListener { selectPayFrequency() }

        bank_account_type.setOnClickListener { selectBankAccountType() }
        bankAccountType.editText?.setOnClickListener { selectBankAccountType() }

        income_type.setOnClickListener { selectIncomeType() }
        incomeType.editText?.setOnClickListener { selectIncomeType() }

        militaryInput.setOnClickListener { selectMilitaryStatus() }
        military.editText?.setOnClickListener { selectMilitaryStatus() }

        own_home.setOnClickListener { selectResidenceStatus() }
        ownHome.editText?.setOnClickListener { selectResidenceStatus() }

        direct_deposit.setOnClickListener { selectDepositStatus() }
        directDeposit.editText?.setOnClickListener { selectDepositStatus() }
    }

    private fun selectTimeToCall() {
        showCustomDialog(timeToCallList, timeToCall.editText)
        timeToCall.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dialog?.cancel()
            }
        })
    }

    private fun selectPayFrequency() {
        showCustomDialog(payFrequencyList, payFrequency.editText)
        payFrequency.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dialog?.cancel()
            }
        })
    }

    private fun selectBankAccountType() {
        showCustomDialog(bankAccountTypeList, bankAccountType.editText)
        bankAccountType.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dialog?.cancel()
            }
        })
    }

    private fun selectIncomeType() {
        showCustomDialog(incomeTypeList, incomeType.editText)
        incomeType.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dialog?.cancel()
            }
        })
    }

    private fun selectMilitaryStatus() {
        showCustomDialog(militaryStatusList, military.editText)
        military.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dialog?.cancel()
            }
        })
    }

    private fun selectResidenceStatus() {
        showCustomDialog(residenceStatusList, ownHome.editText)
        ownHome.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dialog?.cancel()
            }
        })
    }

    private fun selectDepositStatus() {
        showCustomDialog(depositStatusList, directDeposit.editText)
        directDeposit.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
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
                ctx -> ContextCompat.getColor(ctx, R.color.orange)
            }.let {
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
        }else{
            next.isEnabled = false
        }
    }

    fun sendData(){

        var residence = ownHome.editText?.text.toString()
        val rArr = resources.getStringArray(R.array.residence_status)
        when (residence) {
            rArr[0] -> residence = "1"
            rArr[1] -> residence = "0"
        }

        var militaryStatus = military.editText?.text.toString()
        val mArr = resources.getStringArray(R.array.military_status)
        when (militaryStatus) {
            mArr[0] -> militaryStatus = "1"
            mArr[1] -> militaryStatus = "0"
        }

        var depositStatus = directDeposit.editText?.text.toString()
        val dArr = resources.getStringArray(R.array.deposit_status)
        when (depositStatus) {
            dArr[0] -> depositStatus = "1"
            dArr[1] -> depositStatus = "0"
        }

        Thread(Runnable {
            runOnUiThread {
                showProgressDialog("Sending...")
            }
            val response: Response<CustomResponse> = APICaller.paydayAPI.createLoan("application/json",
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
                    incomeType.editText?.text!!.toString(), session_id, referrer, domain, user_agent,
                    client_ip_address, affiliate_id, api_key, tier_key).execute()
            if (response.isSuccessful){
                val responseBody = response.body()
                if (responseBody!!.status == "error"){
                    runOnUiThread {
                        Crashlytics.log(response.toString())
                        dialog?.dismiss()
                        Toast.makeText(baseContext, responseBody.message, Toast.LENGTH_LONG).show()
                    }
                }else if (responseBody.status == "success"){
                    runOnUiThread {
                        dialog?.dismiss()
                        Toast.makeText(baseContext, "Success", Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                runOnUiThread {
                    dialog?.dismiss()
                    Toast.makeText(baseContext, "Server error, try again later", Toast.LENGTH_LONG).show()
                }
            }
        }).start()
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
        dialog?.window?.setLayout(600, 290)
    }

    private inner class SingleWatcher : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, after: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, before: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when {
                isEqual(requestedAmount, editable) -> {
                    requestedAmountFilled = true
                }
                isEqual(firstName, editable) -> {
                    firstNameFilled = true
                }
                isEqual(lastName, editable) -> {
                    lastNameFilled = true
                }
                isEqual(zip, editable) -> {
                    zipFilled = true
                }
                isEqual(state, editable) -> {
                    stateFilled = true
                }
                isEqual(city, editable) -> {
                    cityFilled = true
                }
                isEqual(address, editable)  -> {
                    addressFilled = true
                }
                isEqual(addressSince, editable) -> {
                    addressSinceFilled = true
                }
                isEqual(birthDate, editable) -> {
                    birthDateFilled = true
                }
                isEqual(email, editable) -> {
                    emailFilled = true
                }
                isEqual(ownHome, editable) -> {
                    ownHomeFilled = true
                }
                isEqual(homePhone, editable) -> {
                    homePhoneFilled = true
                }
                isEqual(workPhone, editable) -> {
                    workPhoneFilled = true
                }
                isEqual(military, editable) -> {
                    militaryFilled = true
                }
                isEqual(timeToCall, editable) -> {
                    timeToCallFilled = true
                }
                isEqual(employer, editable) -> {
                    employerFilled = true
                }
                isEqual(jobTitle, editable) -> {
                    jobTitleFilled = true
                }
                isEqual(employedMonth, editable) -> {
                    employedMonthFilled = true
                }
                isEqual(monthlyIncome, editable) -> {
                    monthlyIncomeFilled = true
                }
                isEqual(payDate1, editable) -> {
                    payDate1Filled = true
                }
                isEqual(payDate2, editable) -> {
                    payDate2Filled = true
                }
                isEqual(payFrequency, editable) -> {
                    payFrequencyFilled = true
                }
                isEqual(driversLicense, editable) -> {
                    driversLicenseFilled = true
                }
                isEqual(driversLicenseState, editable) -> {
                    driversLicenseStateFilled = true
                }
                isEqual(bankName, editable) -> {
                    bankNameFilled = true
                }
                isEqual(bankPhone, editable) -> {
                    bankPhoneFilled = true
                }
                isEqual(bankAba, editable) -> {
                    bankAbaFilled = true
                }
                isEqual(bankAccount, editable) -> {
                    bankAccountFilled = true
                }
                isEqual(bankAccountType, editable) -> {
                    bankAccountTypeFilled = true
                }
                isEqual(directDeposit, editable) -> {
                    directDepositFilled = true
                }
                isEqual(ssn, editable) -> {
                    ssnFilled = true
                }
                isEqual(bankAccountSince, editable) -> {
                    bankAccountSinceFilled = true
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