package com.example.gastos.service;

import com.example.gastos.model.Group;
import com.example.gastos.model.GroupMember;
import com.example.gastos.model.User;
import com.example.gastos.repository.GroupMemberRepository;
import com.example.gastos.repository.GroupRepository;
import com.example.gastos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    public Group createGroup(String name, String description, User admin) {
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setAdmin(admin);
        group = groupRepository.save(group);

        // The admin is also a member
        addMember(group, admin, true);

        return group;
    }

    public void addMember(Group group, User user, boolean canAddExpenses) {
        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        member.setCanAddExpenses(canAddExpenses);
        groupMemberRepository.save(member);
    }

    public void addMemberByUsername(Group group, String username, boolean canAddExpenses) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        addMember(group, user, canAddExpenses);
    }

    public List<Group> findGroupsByUser(User user) {
        return groupMemberRepository.findAll().stream()
                .filter(gm -> gm.getUser().getId().equals(user.getId()))
                .map(GroupMember::getGroup)
                .collect(Collectors.toList());
    }

    public Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }
}
