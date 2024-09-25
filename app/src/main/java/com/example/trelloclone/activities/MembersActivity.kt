package com.example.trelloclone.activities

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.firebase.FirestoreClass
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.Constants
import com.projemanag.adapters.MemberListItemsAdapter

class MembersActivity : BaseActivity() {
    private lateinit var mBoardDetails: Board
    private lateinit var mAssignedMembersList: ArrayList<User>
    private var anyChangesMade: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            val boardDetail = intent.getParcelableExtra<Board>(Constants.BOARD_DETAIL)

            if (boardDetail != null) {
                mBoardDetails = boardDetail
                showProgressDialog(resources.getString(R.string.please_wait))

                if (!mBoardDetails.assignedTo.isNullOrEmpty()) {
                    FirestoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
                } else {
                    hideProgressDialog()
                    Toast.makeText(this, "Board detail error: No members assigned.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle null case if boardDetail is null
                Toast.makeText(this, "Error: Board details are missing.", Toast.LENGTH_SHORT).show()
                Log.e("MembersActivity", "Error: Board details are missing.")
                finish() // Optionally close the activity
            }
        } else {
            // Handle case where intent doesn't have BOARD_DETAIL extra
            Toast.makeText(this, "Error: No board details passed.", Toast.LENGTH_SHORT).show()
            Log.e("MembersActivity", "Error: No board details passed.")
            finish() // Optionally close the activity
        }

        setupActionBar()
    }


    fun setupMembersList(list: ArrayList<User>){
        mAssignedMembersList = list
        hideProgressDialog()

        findViewById<RecyclerView>(R.id.rv_members_list).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.rv_members_list).setHasFixedSize(true)

        val adapter = MemberListItemsAdapter(this, list)
        findViewById<RecyclerView>(R.id.rv_members_list).adapter = adapter

    }

    private fun setupActionBar() {

        val toolbar_members_activity = findViewById<Toolbar>(R.id.toolbar_members_activity)
        setSupportActionBar(toolbar_members_activity)

        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }

        toolbar_members_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_member, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_member->{
                dialogSearchMember()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun dialogSearchMember() {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_search_member)
        dialog.findViewById<TextView>(R.id.tv_add).setOnClickListener(View.OnClickListener {

            val email = dialog.findViewById<EditText>(R.id.et_email_search_member).text.toString()

            if (email.isNotEmpty()) {
                dialog.dismiss()
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getMemberDetails(this@MembersActivity, email)
            } else {
                showErrorSnackBar("Please enter members email address.")
            }
        })
        dialog.findViewById<TextView>(R.id.tv_cancel).setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }


    fun memberDetails(user: User){
        mBoardDetails.assignedTo.add(user.id)
        FirestoreClass().assignMemberToBoard(this@MembersActivity, mBoardDetails, user)
    }

    override fun onBackPressed() {
        if(anyChangesMade){
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    fun memberAssignSuccess(user: User) {
        hideProgressDialog()
        mAssignedMembersList.add(user)
        anyChangesMade = true
        setupMembersList(mAssignedMembersList)
        // TODO (Step 5: Call the AsyncTask class when the board is assigned to the user and based on the users detail send them the notification using the FCM token.)
        //SendNotificationToUserAsyncTask(mBoardDetails.name, user.fcmToken).execute()
    }


}