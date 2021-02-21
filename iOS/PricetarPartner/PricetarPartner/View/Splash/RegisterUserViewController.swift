//
//  RegisterUserViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class RegisterUserViewController: UIViewController {

    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var passwordTextField: UITextField!
    
    @IBAction func didEndOnExitTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        
        self.view.endEditing(true)
        self.pop(animationType: .horizontal)
    }

    @IBAction func onTapRegister(_ sender: Any) {

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
        
        RegisterUserRequester.register(email: email, password: password, completion: { result, userId in
            Loading.stop()
            
            if result, let userId = userId {
                let saveData = SaveData.shared
                saveData.userId = userId
                saveData.save()
                
                let profile = self.instantiate(storyboard: "MyPage", identifier: "ProfileViewController") as! ProfileViewController
                self.stack(viewController: profile, animationType: .horizontal)
            } else {
                Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
}
