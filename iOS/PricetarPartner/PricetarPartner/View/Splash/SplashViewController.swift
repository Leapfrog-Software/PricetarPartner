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
    
    private func fetch() {
        
        FetchUserRequester.shared.fetch(completion: { result in
            
        })
        self.showLoginView()
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
    }
    
    @IBAction func onTapRegister(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let registerUser = self.instantiate(storyboard: "Main", identifier: "RegisterUserViewController") as! RegisterUserViewController
        self.stack(viewController: registerUser, animationType: .horizontal)
    }
}
