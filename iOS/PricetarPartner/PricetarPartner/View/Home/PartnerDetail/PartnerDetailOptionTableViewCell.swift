//
//  PartnerDetailOptionTableViewCell.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/26.
//

import UIKit

class PartnerDetailOptionTableViewCell: UITableViewCell {

    @IBOutlet private weak var priceLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var separatorView: UIView!

    func configure(price: String, message: String) {
        
        self.priceLabel.text = price
        self.messageLabel.text = message
    }
    
    func height(message: String) -> CGFloat {
        
        self.configure(price: "", message: message)
        
        self.setNeedsLayout()
        self.layoutIfNeeded()
        
        return self.separatorView.frame.origin.y + self.separatorView.frame.size.height
    }
}
