package se.yrgo.services.calls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.domain.Action;
import se.yrgo.domain.Call;
import se.yrgo.services.customers.CustomerManagementService;
import se.yrgo.services.customers.CustomerNotFoundException;
import se.yrgo.services.diary.DiaryManagementService;

import java.util.Collection;

@Service
public class CallHandlingServiceImpl implements CallHandlingService {

    @Autowired
    private CustomerManagementService customerService;

    @Autowired
    private DiaryManagementService diaryService;


    @Override
    @Transactional
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException {
        customerService.recordCall(customerId, newCall);

        if (actions != null) {
            for (Action action : actions) {
                diaryService.recordAction(action);
            }
        }
    }
}
