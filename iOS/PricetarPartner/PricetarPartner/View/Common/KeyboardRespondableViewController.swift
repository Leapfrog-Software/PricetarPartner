//
//  KeyboardRespondableViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/22.
//

import UIKit

struct KeyboardAnimationOption {
    let height: CGFloat
    let duration: TimeInterval
    let curve: UIView.AnimationOptions
}

class KeyboardRespondableViewController: UIViewController {
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        let notificationCenter = NotificationCenter.default
        
        notificationCenter.addObserver(self, selector: #selector(keyboardWillShow(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        notificationCenter.addObserver(self, selector: #selector(keyboardWillHide(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        let notificationCenter = NotificationCenter.default
        notificationCenter.removeObserver(self, name: UIResponder.keyboardWillShowNotification, object: nil)
        notificationCenter.removeObserver(self, name: UIResponder.keyboardWillHideNotification, object: nil)
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        self.didChangeKeyboard(option: self.animation(from: notification))
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        self.didChangeKeyboard(option: self.animation(from: notification))
    }
    
    private func animation(from: NSNotification) -> KeyboardAnimationOption {
        
        let userInfo = from.userInfo
        let frameEnd = (userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue ?? .zero
        let height = UIScreen.main.bounds.size.height - frameEnd.origin.y
        let duration = (userInfo?[UIResponder.keyboardAnimationDurationUserInfoKey] as? TimeInterval) ?? 0
        let curve = (userInfo?[UIResponder.keyboardAnimationCurveUserInfoKey] as? UIView.AnimationOptions) ?? .curveEaseOut
        return KeyboardAnimationOption(height: height, duration: duration, curve: curve)
    }
    
    func didChangeKeyboard(option: KeyboardAnimationOption) {}
}

