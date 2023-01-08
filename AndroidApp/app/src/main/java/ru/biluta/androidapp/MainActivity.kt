package ru.biluta.androidapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.VERBOSE
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import ru.biluta.androidapp.databinding.ActivityMainBinding
import ru.biluta.androidapp.forRecyclerView.CustomRecyclerAdapterForDepartments
import ru.biluta.androidapp.forRecyclerView.RecyclerItemClickListener
import ru.biluta.androidapp.ui.business.Company
import ru.biluta.androidapp.ui.business.CompanyOperator
import ru.biluta.androidapp.ui.business.DbHelper
import ru.biluta.androidapp.ui.business.Department
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import com.google.gson.GsonBuilder
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DepartmentDetailsDialogFragment.OnInputListenerSortId
{
    private val gsonBuilder = GsonBuilder()
    private val gson: Gson = gsonBuilder.create()
    private val serverIP = "192.168.0.137"
    private val serverPort = 6666
    private lateinit var connection: Connection
    private var connectionStage: Int = 0
    private lateinit var dbh: DbHelper
    private var dbVersion = 2
    private var startTime: Long = 0

    private lateinit var textViewCompanyName: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nv: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerViewDepartments: RecyclerView

    private val coperator: CompanyOperator = CompanyOperator()
    private var currentCompanyID: Int = -1
    private var currentDepartmentID: Int = -1
    private var waitingForUpdate: Boolean = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        textViewCompanyName = findViewById(R.id.textViewCompanyName)
        drawerLayout = binding.drawerLayout
        nv = binding.navView
        nv.setNavigationItemSelectedListener(this)
        toolbar = findViewById(R.id.toolbar)
        toolbar.apply { setNavigationIcon(R.drawable.ic_my_menu) }
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        progressBar = findViewById(R.id.progressBar)
        recyclerViewDepartments = findViewById(R.id.recyclerViewDepartments)
        recyclerViewDepartments.visibility = View.INVISIBLE
        recyclerViewDepartments.layoutManager = LinearLayoutManager(this)

        recyclerViewDepartments.addOnItemTouchListener(
            RecyclerItemClickListener(
                recyclerViewDepartments,
                object: RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        currentDepartmentID = position
                        val departmentDetails = DepartmentDetailsDialogFragment()
                        val tempDepartment = coperator.getDepartment(currentCompanyID, currentDepartmentID)
                        val bundle = Bundle()
                        bundle.putString("departmentName", tempDepartment.nameOfDepartment)
                        bundle.putString("managerFio", tempDepartment.fioManager)
                        bundle.putString("manageStartDate", tempDepartment.managerStartDate)
                        bundle.putString("streetAddress", tempDepartment.streetAddress)
                        bundle.putString("postalCode", tempDepartment.postalCode.toString())
                        bundle.putString("city", tempDepartment.city)
                        bundle.putString("region", tempDepartment.region)
                        bundle.putString("countEmployee", tempDepartment.countEmployee.toString())
                        bundle.putString("connection", connectionStage.toString())
                        departmentDetails.arguments = bundle
                        departmentDetails.show(fragmentManager, "MyCustomDialog")

                        val countEmployee: String = tempDepartment.countEmployee.toString()
                        val text: String = "В этом подразделении числится ${countEmployee} сотрудников."
                        val toast = Toast.makeText(
                            applicationContext,
                            text,
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                        if (connectionStage == 1) {
                            currentDepartmentID = position
                            val manager: FragmentManager = supportFragmentManager
                            val myDialogFragmentDelDepartment = MyDialogFragmentDelDepartment()
                            val bundle = Bundle()
                            bundle.putString(
                                "department",
                                coperator.getDepartment(currentCompanyID, currentDepartmentID).nameOfDepartment
                            )
                            myDialogFragmentDelDepartment.arguments = bundle
                            myDialogFragmentDelDepartment.show(manager, "MyDialog")
                        } else {
                            val toast = Toast.makeText(
                                applicationContext,
                                "Приложение не в сети!",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }
                }
            )
        )
        dbh = DbHelper(this, "MyFirstDB", null, dbVersion)
        startTime = System.currentTimeMillis()
        connection = Connection(serverIP, serverPort, "{R}", this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (currentCompanyID != -1) {
            menu.getItem(0).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_add) {
            val intent = Intent()
            intent.setClass(this, EditDepartmentActivity::class.java)
            intent.putExtra("action", 1)
            startActivityForResult(intent, 1)
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class Connection(
        private val SERVER_IP: String,
        private val SERVER_PORT: Int,
        private val refreshCommand: String,
        private val activity: Activity
    ) {
        private var outputServer: PrintWriter? = null
        private var inputServer: BufferedReader? = null
        var thread1: Thread? = null
        private var threadT: Thread? = null

        internal inner class Thread1Server : Runnable {
            override fun run()
            {
                val socket: Socket
                try {
                    socket = Socket(SERVER_IP, SERVER_PORT)
                    outputServer = PrintWriter(socket.getOutputStream())
                    inputServer = BufferedReader(InputStreamReader(socket.getInputStream()))
                    Thread(Thread2Server()).start()
                    sendDataToServer(refreshCommand)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        internal inner class Thread2Server : Runnable {
            override fun run() {
                while (true) {

                    try {
                        val message = inputServer!!.readLine()
                        if (message != null)
                        {
                            activity.runOnUiThread { processingInputStream(message) }
                        } else {
                            thread1 = Thread(Thread1Server())
                            thread1!!.start()
                            return
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        internal inner class Thread3Server(private val message: String) : Runnable
        {
            override fun run()
            {
                outputServer!!.write(message)
                outputServer!!.flush()
            }
        }

        internal inner class ThreadT : Runnable
        {
            override fun run() {
                while (true)
                {
                    if (System.currentTimeMillis() - startTime > 5000L && connectionStage == 0)
                    {
                        activity.runOnUiThread { val toast = Toast.makeText(
                            applicationContext,
                            "Подключиться не удалось!\n" +
                                    "Будут использоваться данные из локальной базы данных.",
                            Toast.LENGTH_LONG
                        )
                            toast.show() }
                        connectionStage = -1
                        progressBar.visibility = View.INVISIBLE
                        val tempArrayListCompanies: ArrayList<Company> = dbh.getAllData()
                        coperator.setCompanies(tempArrayListCompanies)
                        for (i in 0 until tempArrayListCompanies.size)
                        {
                            activity.runOnUiThread { nv.menu.add(0, i, 0,
                                tempArrayListCompanies[i].name as CharSequence) }
                        }
                    }
                }
            }
        }

        fun sendDataToServer(text: String)
        {
            Thread(Thread3Server(text + "\n")).start()
        }

        private fun processingInputStream(text: String)
        {
            dbh.removeAllData()
            val tempGo: CompanyOperator = gson.fromJson(text, CompanyOperator::class.java)
            for (i in tempGo.getCompanies())
            {
                dbh.insertCompany(i)
            }

            if (connectionStage != 1)
            {
                val toast = Toast.makeText(
                    applicationContext,
                    "Успешно подключено!\n" +
                            "Будут использоваться данные, полученные от сервера.",
                    Toast.LENGTH_LONG
                )
                toast.show()
            }

            progressBar.visibility = View.INVISIBLE
            for (i in 0 until coperator.getCompanies().size)
            {
                nv.menu.removeItem(i)
            }
            val tempArrayListCompanies: ArrayList<Company> = dbh.getAllData()
            coperator.setCompanies(tempArrayListCompanies)
            for (i in 0 until tempArrayListCompanies.size)
            {
                nv.menu.add(
                    0, i, 0,
                    tempArrayListCompanies[i].name as CharSequence
                )
            }
            if (waitingForUpdate || connectionStage == -1)
            {
                waitingForUpdate = false
                if (currentCompanyID != -1)
                {
                    recyclerViewDepartments.adapter = CustomRecyclerAdapterForDepartments(
                        coperator.getDepartmentNames(currentCompanyID),
                        coperator.getStreetAddress(currentCompanyID)
                    )
                }
            }
            connectionStage = 1
        }

        init {
            thread1 = Thread(Thread1Server())
            thread1!!.start()
            threadT = Thread(ThreadT())
            threadT!!.start()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val tempString = "Предприятие ${item.title}"
        textViewCompanyName.text = tempString
        invalidateOptionsMenu()
        currentCompanyID = item.itemId
        recyclerViewDepartments.adapter = CustomRecyclerAdapterForDepartments(coperator.getDepartmentNames(currentCompanyID),
        coperator.getStreetAddress(currentCompanyID))
        recyclerViewDepartments.visibility = View.VISIBLE
        return true
    }

    fun delDepartment() {
        connection.sendDataToServer("d$currentCompanyID,$currentDepartmentID")
        waitingForUpdate = true
    }

    override fun sendInputSortId(sortId: Int) {
        var text: String = "Выбрана сортировка подразделений по "
        when (sortId) {
            0 -> text += "названию."
            1 -> text += "ФИО менеджера."
            2 -> text += "дате начала работы менеджера."
            3 -> text += "городскому адресу."
            4 -> text += "почтовому индексу."
            5 -> text += "городу."
            6 -> text += "региону."
            7 -> text += "количеству сотрудников."
            9 -> text = "Внимание! Изменения будут отправлены на сервер."
        }
        if (sortId != -1 && sortId != 8){
            val toast = Toast.makeText(
                applicationContext,
                text,
                Toast.LENGTH_LONG
            )
            toast.show()
        }
        if (sortId > -1 && sortId < 8) {
            coperator.sortDepartments(currentCompanyID, sortId)
            if (connectionStage == 1) {
                connection.sendDataToServer("u" + gson.toJson(coperator))
            }
        }
        if (sortId == 8) {
            val manager: FragmentManager = supportFragmentManager
            val myDialogFragmentDelDepartment = MyDialogFragmentDelDepartment()
            val bundle = Bundle()
            bundle.putString("department", coperator.getDepartment(currentCompanyID, currentDepartmentID).nameOfDepartment)
            myDialogFragmentDelDepartment.arguments = bundle
            myDialogFragmentDelDepartment.show(manager, "myDialog")
        }
        if (sortId == 9) {
            val tempDepartment = coperator.getDepartment(currentCompanyID, currentDepartmentID)
            val intent = Intent()
            intent.setClass(this, EditDepartmentActivity::class.java)
            intent.putExtra("action", 2)
            intent.putExtra("department", tempDepartment.nameOfDepartment)
            intent.putExtra("manager", tempDepartment.fioManager)
            intent.putExtra("managerstartdate", tempDepartment.managerStartDate)
            intent.putExtra("streetaddress", tempDepartment.streetAddress)
            intent.putExtra("postalcode", tempDepartment.postalCode.toString())
            intent.putExtra("city", tempDepartment.city)
            intent.putExtra("region", tempDepartment.region)
            intent.putExtra("countemployee", tempDepartment.countEmployee.toString())
            startActivityForResult(intent, 1)
        }
        recyclerViewDepartments.adapter = CustomRecyclerAdapterForDepartments (
            coperator.getDepartmentNames(currentCompanyID),
            coperator.getStreetAddress(currentCompanyID))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val action = data?.getSerializableExtra("action") as Int
            val departmentName = data.getSerializableExtra("department") as String
            val managerFio = data.getSerializableExtra("manager") as String
            val managerStartDate = data.getSerializableExtra("managerstartdate") as String
            val streetAddress = data.getSerializableExtra("streetaddress") as String
            val postalCode = data.getSerializableExtra("postalcode") as Int
            val city = data.getSerializableExtra("city") as String
            val region = data.getSerializableExtra("region") as String
            val countEmployee = data.getSerializableExtra("countemployee") as Int
            val tempDepartment = Department(departmentName, managerFio, managerStartDate,
                streetAddress, postalCode, city, region, countEmployee)
            val tempDepartmentJSON: String = gson.toJson(tempDepartment)

            if (action == 1) {
                val tempStringToSend = "a${coperator.getCompanies()[currentCompanyID].name}##$tempDepartmentJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
            if (action == 2) {
                val tempStringToSend = "e$currentCompanyID.$currentDepartmentID##$tempDepartmentJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
        }
    }
}