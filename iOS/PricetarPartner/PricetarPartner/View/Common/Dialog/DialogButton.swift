//
//  DialogButton.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class DialogButton: UIView {
    
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var button: UIButton!
    
    private var didTap: (() -> ())?
    
    func configure(title: String, color: UIColor, didTap: @escaping (() -> ())) {
        self.titleLabel.text = title
        self.button.backgroundColor = color
        self.didTap = didTap
    }
    
    @IBAction func onTap(_ sender: Any) {
        self.didTap?()
    }
}
