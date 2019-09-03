package unit;

import com.t_systems.webstore.dao.UserDao;
import com.t_systems.webstore.model.dto.UserDto;
import com.t_systems.webstore.model.entity.User;
import com.t_systems.webstore.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.*;

public class UserServiceTests {

    private UserDao userDao = mock(UserDao.class);

    private ModelMapper modelMapper = new ModelMapper();

    private UserService userService = new UserService(userDao,modelMapper);

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void changeUserTest() throws ParseException {
        String username = "sample";
        when(userDao.getUser(username)).thenReturn(new User());

        UserDto userDto = new UserDto();
        userDto.setFlat("flat");
        userDto.setHouse("house");
        userDto.setStreet("street");
        userDto.setCountry("country");
        userDto.setPassword("password");
        userDto.setCity("city");
        userDto.setConfirm("confirm");
        userDto.setDateOfBirth("2000-01-01");
        userDto.setEmail("email");
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setUsername("username");

        userService.changeUser(username,userDto);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).updateUser(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        Assert.assertEquals("flat",user.getAddress().getFlat());
        Assert.assertEquals("house",user.getAddress().getHouse());
        Assert.assertEquals("street",user.getAddress().getStreet());
        Assert.assertEquals("country",user.getAddress().getCountry());
        Assert.assertEquals("city",user.getAddress().getCity());
        Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"),
                user.getDateOfBirth());
        Assert.assertEquals("email",user.getEmail());
        Assert.assertEquals("firstName",user.getFirstName());
        Assert.assertEquals("lastName", user.getLastName());
        Assert.assertEquals("username",user.getUsername());
    }
}
