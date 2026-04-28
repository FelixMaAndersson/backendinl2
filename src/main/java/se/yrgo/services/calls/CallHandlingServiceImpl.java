package se.yrgo.services.calls;

import org.springframework.transaction.annotation.Transactional;
import se.yrgo.domain.Action;
import se.yrgo.domain.Call;
import se.yrgo.services.customers.CustomerManagementService;
import se.yrgo.services.customers.CustomerNotFoundException;
import se.yrgo.services.diary.DiaryManagementService;

import java.util.Collection;

public class CallHandlingServiceImpl implements CallHandlingService {

    private CustomerManagementService customerService;
    private DiaryManagementService diaryService;

    public void setCustomerService(CustomerManagementService customerService) {
        this.customerService = customerService;
    }

    public void setDiaryService(DiaryManagementService diaryService) {
        this.diaryService = diaryService;
    }

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
