# DataBase : ( save info in the machine )
* table :
```
@Entity (tableName = "contacts" )
data class Contact(
 @PrimaryKey(autoGenerate = true)
 val uid: Int = 0,
 val prenom: String,
 val nom: String,
 val telephone: String
)
```
* DAO : 
```
@Dao
interface ContactDao {  
    @Insert(onConflict = OnConflictStrategy.REPLACE)  
    suspend fun insert(contact: Contact)  

    @Update  
    suspend fun update(contact: Contact)     
       
    @Delete  
    suspend fun delete(contact: Contact)  

    @Query("SELECT * FROM contacts")  
    fun getAll(): Flow<List<Contact>>  

    @Query("SELECT * FROM contacts WHERE uid IN (:userIds)")  
    fun loadAllByIds(userIds: IntArray): Flow<List<Contact>>  
}
```

* Database : 
```
@Database(entities = [Contact::class], version = 1, exportSchema = false)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun contactDao(): ContactDao
        companion object {
            @Volatile
                private var Instance: AppDatabase? = null
                fun getDatabase(context: Context): AppDatabase =
                    Instance ?: synchronized(this) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "contact_database"
                ).build().also { Instance = it }
        }
    }
}
```

* ViewModel : 
```
class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: ContactDao = AppDatabase.getDatabase(application.applicationContext).  contactDao()  
    val contacts: StateFlow<List<Contact>> = dao.getAll()  
    .stateIn(
        scope = viewModelScope,  
        started = SharingStarted.WhileSubscribed(5000),  
        initialValue = emptyList()  
    )  
    fun add(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {  
        dao.insert(contact)  
    }
    fun update(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {  
        dao.update(contact)  
    }  
    fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {  
        dao.delete(contact)  
    }  
}
```
-> For using : 
```
private val contactViewModel: ContactViewModel by viewModels()
ListeContactApp(contactViewModel)
val contacts by viewModel.contacts.collectAsState()
```

# Retrofit (access online url)
* class creation : 
```
@Serializable
data class Todo(
@SerialName("userId") val idUtilisateur: Int,
@SerialName("id") val idTodo: Int,
@SerialName("title") val titre: String,
@SerialName("completed") val complete: Boolean
)
```
* ApiClient 
```
object ApiClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private val retrofit by lazy {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()
    }
        val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
        }
    }
```
* ApiService : "end-point"
```
interface ApiService {
    @GET("todos")  
    suspend fun getTodos(): List<Todo>  
 
//With Query :   
    @GET("todos")  
    suspend fun getTodosByUser(@Query("userId") userId: Int): List<Todo>  

// With path :   
    @GET("todos/{id}")  
    suspend fun getTodoById(@Path("id") id: Int): Todo  
}  
```
* ViewModel :
```  
class TodosViewModel : ViewModel() {  
    val todos = MutableStateFlow<List<Todo>>(emptyList())  
    fun loadTodos() {  
        viewModelScope.launch {  
            try {  
                todos.value = ApiClient.apiService.getTodos()  
            } catch (e: Exception) {  
                Log.e("TodoVM"  
                , "Error: ${e.message}")  
            }
        }  
    }  
}
```
-> To use: 
```
val viewModel: TodosViewModel = viewModel()
val todos by viewModel.todos.collectAsState()
viewModel.loadTodos()
```


# Navigation : 
```
@Composable
fun AppNav() {
 val navController = rememberNavController()
 NavHost(navController, startDestination = "home") {
 composable("home") { HomeScreen(navController) }
 composable("details") { DetailsScreen() }
 }
}
```
-> to use :   
    - navController.navigate("name")  
    - navController.popBackStack()

# ViewModel : 
```
import android.util.Log  
import androidx.lifecycle.ViewModel  
import androidx.lifecycle.viewModelScope  
import ca.qc.labo2formatif2.Data.Livre  
import kotlinx.coroutines.flow.MutableStateFlow  
import kotlinx.coroutines.launch  

class LivreViewModel : ViewModel() {  
    val livres = MutableStateFlow<List<Livre>>(emptyList())  
    fun loadLivres(title: String) {  
        viewModelScope.launch {  
            try {  
                livres.value = (ApiClient.apiService.getLivres(title)).livres  
            } catch (e: Exception) {  
                Log.e("LivreVM"  
                    , "Error: ${e.message}")  
            }  
        }  
    }  
}  
```

# Scaffold : 
* Top Bar : 
```
Scaffold(  
        topBar = {  
            TopAppBar(  
                title = { Text("Recherche de livres") },  

                //Add color to the top bar   
                colors = TopAppBarDefaults.topAppBarColors(  
                    containerColor = MaterialTheme.colorScheme.primary,  
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,  
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary  
                )  
            )  
            // add maybe button  
        }  
){padding ->   
    Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {     
    // code
} }  
```
* Bottom bar :  
```
bottomBar = (containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary){  
    BottomAppBar {  
        IconButton(onClick = { /*Faire quelque chose*/ }) {  
        Icon(Icons.Default.Home, contentDescription = "Accueil")  
    }  
        IconButton(onClick = { /*Faire quelque chose*/ }) {  
        Icon(Icons.Default.Search, contentDescription = "Recherche")  
    }  
        IconButton(onClick = { /*Faire quelque chose*/ }) {  
        Icon(Icons.Default.Settings, contentDescription = "Paramètres")  
    }}  
}
```

* DropDownMenu : 
```
TopAppBar(    
    title = { Text("App avec menu") },  
    actions = {  
        IconButton(onClick = { expanded = true }) {  
        Icon(Icons.Default.MoreVert, contentDescription = "Menu")  
    }  
    DropdownMenu(  
        expanded = expanded,   
        onDismissRequest = { expanded = false }  
    ) {  
    DropdownMenuItem(  
        text = { Text("Option 1") },  
        onClick = { expanded = false }  
     )  
    DropdownMenuItem(  
        text = { Text("Option 2") },  
        onClick = { expanded = false }  
    )}}  
)  
```


# LazyList :
item { } pour présenter 1 élément  
items(items = shows) { } pour présenter une liste  

* a list : 
```
LazyColumn {  
    items(items = shows) { show ->  
        Row(
            modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(show.title)
                    Text("${show.network} • ${show.year}")
                }
                if (show.isFavorite) Text("★")
            }
            Divider()
    }  
}
```
* A textField : 
```
var title by remember { mutableStateOf(show.title) } // Important

Row(modifier = Modifier.fillMaxWidth(),    
        horizontalArrangement = Arrangement.Center,  
        verticalAlignment = Alignment.CenterVertically) {  
                OutlinedTextField(  
                    value = title,  
                    onValueChange = { title = it },  
                    label = { Text(text = "Titre du livre") }  
                )  
        }  
```


# Short resume of all the function and declaration for Kotlin 
## Alignement : 
* Row : (vertical position)
```
Row(modifier = Modifier.fillMaxHeight()) {
 Text(text = "Top", modifier = Modifier.align(Alignment.Top))   
 Text(text = "Center", modifier = Modifier.align(Alignment.CenterVertically))   
 Text(text = "Bottom", modifier = Modifier.align(Alignment.Bottom))  
}
```

* Column : (horizontal position)
```
Column(modifier = Modifier.fillMaxSize()) {
 Text("Start", modifier = Modifier.align(Alignment.Start))  
 Text("Center", modifier = Modifier.align(Alignment.CenterHorizontally))  
 Text("End", modifier = Modifier.align(Alignment.End))  
}
```

* Box : (everywhere we want)
```
Box(modifier = Modifier.fillMaxSize()) {
 Text("Top left"
, modifier = Modifier.align(Alignment.TopStart))  
 Text("Center", modifier = Modifier.align(Alignment.Center))  
 Text("Bottom right", modifier = Modifier.align(Alignment.BottomEnd))  
}
```

## Images : (painterResource)
* Normal way : 
```
val image = painterResource(R.mipmap.ic_launcher)
Image(
 painter = image,
 contentDescription =
""
)
```

* As a background : 
```
Box(modifier = modifier) {
 Image(
 painter = image,
 contentDescription =
""
 )
 Text(text = "Je suis un texte")
}
```

## LogCat : 
 Log.e(String, String) (error)  
 Log.w(String, String) (warning)  
 Log.i(String, String) (information)  
 Log.d(String, String) (debug)  
 Log.v(String, String) (verbose)  
```
val TAG = "MY_APP"  
Button(onClick = {  
    Log.e(TAG, "ce qui se passe présentement")}  
```

## Toast : (tempory message)
```
@Composable
fun ExempleToast() {  
 val context = LocalContext.current  
 Button(onClick = {  
 Toast.makeText(context, "Bonjour depuis un Toast !", Toast.LENGTH_SHORT).show()  
 }) {  
 Text("Afficher un Toast")  
 }  
}
```

## Click : 
* Long press : 
```
Box(modifier = Modifier.fillMaxSize()  
 .pointerInput(Unit) {  

    detectTapGestures(  
    onLongPress = { offset ->  
    println("Pression longue à $offset")  

 })}.background(Color.Gray)
)
```

* Simple / Double tap : 
``` 
Modifier.pointerInput(Unit) {  
 detectTapGestures(  
 onTap = { println("Tap simple") },  
 onDoubleTap = { println("Double tap") }  
 )  
}  
```

* Swipe/ drag : 
```
Modifier.pointerInput(Unit) {  
 detectDragGestures { change, dragAmount ->  
 change.consume()  
 println("Glissement de $dragAmount")  
 }  
}  
```