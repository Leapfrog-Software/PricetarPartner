//
//  Dialog.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

struct DialogAction {
    
    let title: String
    let action: (() -> ())?
    let color: Dialog.ActionColor?
    
    init(title: String, action: (() -> ())?, color: Dialog.ActionColor? = nil) {
        self.title = title
        self.action = action
        self.color = color
    }
}

class Dialog: UIView {
    
    enum Style {
        case success
        case error
    }
    
    enum ActionColor {
        case success
        case error
        case cancel
        
        func toColor() -> UIColor {
            switch self {
            case .success:
                return UIColor(red: 123 / 255, green: 209 / 255, blue: 249 / 255, alpha: 1)
            case .error:
                return UIColor(red: 230 / 255, green: 73 / 255, blue: 66 / 255, alpha: 1)
            case .cancel:
                return UIColor(red: 200 / 255, green: 200 / 255, blue: 200 / 255, alpha: 1)
            }
        }
    }
    
    @IBOutlet private weak var iconImageView: UIImageView!
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var buttonsStackView: UIStackView!
    
    class func show(style: Style, title: String, message: String, actions: [DialogAction]) {
        
        guard let window = UIApplication.shared.keyWindow else {
            return
        }
        
        let dialog = UINib(nibName: "Dialog", bundle: nil).instantiate(withOwner: nil, options: nil).first as! Dialog
        window.addSubview(dialog)
        dialog.translatesAutoresizingMaskIntoConstraints = false
        dialog.topAnchor.constraint(equalTo: window.topAnchor).isActive = true
        dialog.leadingAnchor.constraint(equalTo: window.leadingAnchor).isActive = true
        dialog.trailingAnchor.constraint(equalTo: window.trailingAnchor).isActive = true
        dialog.bottomAnchor.constraint(equalTo: window.bottomAnchor).isActive = true
        
        dialog.configure(style: style, title: title, message: message, actions: actions)
        
        dialog.alpha = 0
        UIView.animate(withDuration: 0.15) {
            dialog.alpha = 1.0
        }
    }
    
    private func configure(style: Style, title: String, message: String, actions: [DialogAction]) {
        
        self.iconImageView.image = UIImage(named: (style == .success) ? "dialog_success" : "dialog_error")
        self.titleLabel.text = title
        self.messageLabel.text = message
        
        actions.forEach { action in
            let button = UINib(nibName: "DialogButton", bundle: nil).instantiate(withOwner: nil, options: nil).first as! DialogButton
            
            let color: UIColor
            if let actionColor = action.color {
                color = actionColor.toColor()
            } else {
                color = (style == .success) ? ActionColor.success.toColor() : ActionColor.error.toColor()
            }
            
            button.configure(title: action.title, color: color, didTap: { [weak self] in
                action.action?()
                UIView.animate(withDuration: 0.15, animations: {
                    self?.alpha = 0
                }, completion: { [weak self] _ in
                    self?.removeFromSuperview()
                })
            })
            self.buttonsStackView.addArrangedSubview(button)
        }
    }
}

