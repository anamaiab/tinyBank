package tinyBank.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import tinyBank.web.model.History;
import tinyBank.web.model.TransactionHistory;
import tinyBank.web.model.TypeTransaction;
import tinyBank.web.model.User;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Controller
@SessionAttributes("user")
public class TinyBankController {
    static HashMap<String, User> userList = new HashMap<>();

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            if (userList.isEmpty() || !userList.containsKey(user.getDocument())) {
                user.setHistory(new History());
                userList.put(user.getDocument(), user);
            } else {
                throw new IllegalArgumentException("This user already exists");
            }
            model.addAttribute("registeredUser", user);
            return "registration_success";
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String document, @RequestParam String password, Model model) {
        try {
            User user = userList.get(document);
            validateLogin(user, password, "Invalid document or password");
            model.addAttribute("user", user);
            return "redirect:/user_data";
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "index";
        }
    }

    @GetMapping("/user_data")
    public String showUserData(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null) {
            return "redirect:/unauthorized";
        }
        try {
            double balance = user.getHistory().getTotalAmount();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.UK);
            String format = currencyFormatter.format(balance);
            model.addAttribute("total_balance", format);
            return "user_data";
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "user_data";
        }
    }

    @PostMapping("/add_money")
    public String addMoney(@SessionAttribute(value = "user", required = false) User user, @RequestParam double amount, @RequestParam String password, Model model) {
        if (user == null) {
            return "redirect:/access_denied";
        }
        try {
            validateLogin(user, password, "Wrong password. It's necessary to send the correct password to transfer amount");
            validateAmount(amount, "You can't deposit negative amount");

            History userHistoryValues = user.getHistory();
            userHistoryValues.addTransaction(amount, TypeTransaction.DEPOSIT);

            return showUserData(user, model);
        } catch (Exception e) {
            setTotalBalance(user, model);
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "user_data";
        }
    }

    @PostMapping("/withdraw_money")
    public String withdrawMoney(@SessionAttribute(value = "user", required = false) User user, @RequestParam double amount, @RequestParam String password, Model model) {
        if (user == null) {
            return "redirect:/access_denied";
        }
        try {
            validateLogin(user, password, "Wrong password. It's necessary to send the correct password to withdrawal amount");
            validateAmount(amount, "You can't withdraw negative amount");

            History userHistoryValues = user.getHistory();

            if (amount > userHistoryValues.getTotalAmount()) {
                throw new IllegalArgumentException("You don't have this amount to withdraw. Your limit is £ " + userHistoryValues.getTotalAmount());
            }

            userHistoryValues.addTransaction(amount, TypeTransaction.WITHDRAWAL);

            return showUserData(user, model);
        } catch (Exception e) {
            setTotalBalance(user, model);
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "user_data";
        }
    }

    @GetMapping("/recent_history")
    public String showRecentHistory(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null) {
            return "redirect:/access_denied";
        }
        try {
            List<TransactionHistory> transactions = user.getHistory().getTransactionHistory();
            model.addAttribute("transactions", transactions);
            return "fragments/transactionHistory";
        } catch (Exception e) {
            setTotalBalance(user, model);
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "user_data";
        }
    }

    @PostMapping("/transfer_credit")
    public String transferCredit(@SessionAttribute(value = "user", required = false) User user, @RequestParam String recipientId, @RequestParam double amount, @RequestParam String password, Model model) {
        if (user == null) {
            return "redirect:/access_denied";
        }
        try {
            validateLogin(user, password, "Wrong password. It's necessary to send the correct password to transfer amount");

            if (user.getDocument().equals(recipientId)) {
                throw new Exception("You can't transfer to yourself");
            }

            if (!userList.containsKey(recipientId)) {
                throw new Exception("The recipient user doesn't exist");
            }

            validateAmount(amount, "You can't transfer negative amount");

            History userHistoryValues = user.getHistory();

            if (amount > userHistoryValues.getTotalAmount()) {
                throw new Exception("You don't have enough balance. Your limit is " + userHistoryValues.getTotalAmount());
            }

            User receivedUser = userList.get(recipientId);
            History recipientHistoryValues = receivedUser.getHistory();

            userHistoryValues.addTransaction(amount, TypeTransaction.TRANSFER_SENT);
            recipientHistoryValues.addTransaction(amount, TypeTransaction.TRANSFER_RECEIVED);

            return showUserData(user, model);
        } catch (Exception e) {
            setTotalBalance(user, model);
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "user_data";
        }
    }

    @PostMapping("/delete_user")
    public String deleteUser(@SessionAttribute(value = "user", required = false) User user, @RequestParam String password, Model model, SessionStatus status) {
        if (user == null) {
            return "redirect:/access_denied";
        }
        try {
            validateLogin(user, password, "Wrong password. It's necessary to send the correct password to delete the account");

            userList.remove(user.getDocument());
            status.setComplete();
            model.addAttribute("message", "User deleted successfully");

            return "redirect:/";
        } catch (Exception e) {
            setTotalBalance(user, model);
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "user_data";
        }
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        status.setComplete();
        return "redirect:/";
    }

    @GetMapping("/unauthorized")
    public String showUnauthorizedPage() {
        return "unauthorized";
    }

    private static void validateAmount(double amount, String messageError) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException(messageError);
        }
    }

    private static void validateLogin(User user, String password, String message) throws Exception {
        if (userList.isEmpty() || user == null || !userList.containsKey(user.getDocument() )) {
            throw new IllegalArgumentException("This user doesn't exist");
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void setTotalBalance(User user, Model model) {
        double balance = user.getHistory().getTotalAmount();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.UK);
        String format = currencyFormatter.format(balance);
        model.addAttribute("total_balance", format);
    }
}
