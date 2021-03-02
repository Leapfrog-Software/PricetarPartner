//
//  SplashViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class SplashViewController: UIViewController {
    
    @IBOutlet private weak var logoVerticalConstraint: NSLayoutConstraint!
    @IBOutlet private weak var loginView: UIView!
    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var passwordTextField: UITextField!
    @IBOutlet private weak var registerView: UIView!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.logoVerticalConstraint.constant = 0
        self.loginView.isHidden = true
        self.loginView.alpha = 0
        self.registerView.isHidden = true
        self.registerView.alpha = 0

        DispatchQueue.main.asyncAfter(deadline: .now() + 1, execute: {
            self.fetch()
        })
    }
    
    func fetch() {
        
        FetchUserRequester.shared.fetch(completion: { resultUser in
            FetchChatGroupRequester.shared.fetch(completion: { resultChatGroup in
                if resultUser && resultChatGroup {
                    if let myUserData = FetchUserRequester.shared.query(userId: SaveData.shared.userId), myUserData.profileType != .none {
                        let tabbar = self.instantiate(storyboard: "Main", identifier: "TabbarViewController") as! TabbarViewController
                        self.stack(viewController: tabbar, animationType: .none)
                    } else {
                        self.showLoginView()
                    }
                } else {
                    let action = DialogAction(title: "OK", action: { [weak self] in
                        self?.fetch()
                    })
                    Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [action])
                }
            })
        })
    }
    
    private func showLoginView() {
        
        self.logoVerticalConstraint.constant = -220
        UIView.animate(withDuration: 0.6, delay: 0, options: .curveEaseInOut, animations: {
            self.view.layoutIfNeeded()
        })
        
        self.loginView.isHidden = false
        self.registerView.isHidden = false
        UIView.animate(withDuration: 0.4, delay: 0.4, options: .curveEaseInOut, animations: {
            self.loginView.alpha = 1.0
            self.registerView.alpha = 1.0
        })
    }
    
    @IBAction func didEndOnExitTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapLogin(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let email = self.emailTextField.text ?? ""
        let password = self.passwordTextField.text ?? ""
        
        if email.count == 0 {
            Dialog.show(style: .error, title: "エラー", message: "メールアドレスの入力がありません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        if password.count == 0 {
            Dialog.show(style: .error, title: "エラー", message: "パスワードの入力がありません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        Loading.start()
        
        LoginRequester.login(email: email, password: password, completion: { result, userId in
            FetchUserRequester.shared.fetch(completion: { _ in
                Loading.stop()
                
                if result, let userId = userId {
                    if let myUserData = FetchUserRequester.shared.query(userId: userId) {
                        let saveData = SaveData.shared
                        saveData.userId = userId
                        saveData.save()
                        
                        if myUserData.profileType != .none {
                            let tabbar = self.instantiate(storyboard: "Main", identifier: "TabbarViewController") as! TabbarViewController
                            self.stack(viewController: tabbar, animationType: .none)
                        } else {
                            let profile = self.instantiate(storyboard: "MyPage", identifier: "ProfileViewController") as! ProfileViewController
                            profile.set(transitionSource: .splash)
                            self.stack(viewController: profile, animationType: .horizontal)
                        }
                        return
                    }
                }
                Dialog.show(style: .error, title: "エラー", message: "ログインに失敗しました", actions: [DialogAction(title: "OK", action: nil)])
            })
        })
    }
    
    @IBAction func onTapRegister(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let registerUser = self.instantiate(storyboard: "Main", identifier: "RegisterUserViewController") as! RegisterUserViewController
        self.stack(viewController: registerUser, animationType: .horizontal)
    }
}
