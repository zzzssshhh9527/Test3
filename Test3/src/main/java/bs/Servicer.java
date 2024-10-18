package bs;


import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Servicer {
    @Autowired
    private ContactRepository contactRepository;

    public List<User> findAllUsers() {
        return contactRepository.findAll();
    }

    public void delete(long id) {
        contactRepository.deleteById(id);
    }

    public void add(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setAddress(userDto.getAddress());
        user.setPhone(userDto.getPhone());
        contactRepository.save(user);
    }

    public void update(UserDto userDto) {
        // 通过ID查找用户实体
        User user = contactRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("未找到ID为 " + userDto.getId() + " 的用户"));

        // 更新用户信息
        user.setName(userDto.getName());
        user.setAddress(userDto.getAddress());
        user.setPhone(userDto.getPhone());

        // 保存更新
        contactRepository.save(user);
    }
}

