//
//  HomeNewUserTableViewCell.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/26.
//

import UIKit

class HomeNewUserTableViewCell: UITableViewCell {

    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var messageLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.userImageView)
        self.userImageView.image = nil
    }
    
    func configure(user: UserData) {
        
        ImageStorage.shared.fetch(url: UserData.imageUrl(userId: user.id), imageView: self.userImageView)
        
        if user.profileType == .client {
            self.messageLabel.text = user.clientMessage
        } else {
            self.messageLabel.text = user.partnerMessage
        }
    }
}
