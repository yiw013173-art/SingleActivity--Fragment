import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.network.model.UserDto
import coil.load // 假设你使用 Coil 加载图片
import coil.transform.CircleCropTransformation
import com.example.myapplicationview.R

/**
 * 适配器泛型传入 UserDto，因为这是列表中每一项的数据模型
 */
class UserAdapter : ListAdapter<UserDto, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // 1. 加载你之前创建的 item_user.xml 布局
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // 2. 获取当前位置的 UserDto 对象
        val user = getItem(position)
        holder.bind(user)
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        private val tvName: TextView = view.findViewById(R.id.tvName)
        private val tvEmail: TextView = view.findViewById(R.id.tvEmail)

        fun bind(user: UserDto) {
            // 3. 绑定文字数据 (拼接名字)
            tvName.text = "${user.name.title} ${user.name.first} ${user.name.last}"
            tvEmail.text = user.email

            // 4. 加载头像图片 (使用 picture.medium 或 large)
            ivAvatar.load(user.picture.medium) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background) // 占位图
                transformations(CircleCropTransformation()) // 圆形剪裁
            }
        }
    }

    /**
     * DiffUtil 用于计算列表差异，避免全局刷新，提升性能
     */
    class UserDiffCallback : DiffUtil.ItemCallback<UserDto>() {
        // 使用唯一标识 uuid 来判断是否是同一个条目
        override fun areItemsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
            return oldItem.login.uuid == newItem.login.uuid
        }

        // 判断内容是否发生变化
        override fun areContentsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
            return oldItem == newItem
        }
    }
}