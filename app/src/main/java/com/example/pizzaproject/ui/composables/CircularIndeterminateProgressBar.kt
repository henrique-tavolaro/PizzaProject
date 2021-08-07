import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable

@Composable
fun CircularIndicator(loading: Boolean){
    if (loading){
        CircularProgressIndicator()
    }
}
