package com.yanbin.service.api.commandhandler;

import com.yanbin.core.content.ThreadWebContextHolder;
import com.yanbin.core.sequence.ISequence;
import com.yanbin.core.sequence.SeqType;
import com.yanbin.service.api.command.CreateUserCommand;
import com.yanbin.service.api.domain.UserDomain;

public class CreateUserHandler implements ICommandHandler<CreateUserCommand> {

    public void handler(CreateUserCommand createUserCommand) {
        ISequence sequence = ThreadWebContextHolder.getContext().getBean("sequenceService");
        Long id = sequence.newKey(SeqType.User);
        UserDomain userDomain = new UserDomain(createUserCommand.getName(), createUserCommand.getMobile(), id);
        userDomain.createUser();
        createUserCommand.setResult(userDomain.getId());
    }
}
