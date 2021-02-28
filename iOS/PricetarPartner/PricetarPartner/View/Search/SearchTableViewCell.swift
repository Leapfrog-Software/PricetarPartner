//
//  SearchTableViewCell.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/28.
//

import UIKit

class SearchTableViewCell: UITableViewCell {

    @IBOutlet private weak var userImageVIew: UIImageView!
    @IBOutlet private weak var messageLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.userImageVIew)
        self.userImageVIew.image = nil
    }
    
    func configure(userData: UserData) {
        
        ImageStorage.shared.fetch(url: UserData.imageUrl(userId: userData.id), imageView: self.userImageVIew)
        
        if userData.profileType == .client {
            self.messageLabel.text = userData.clientMessage
        } else {
            self.messageLabel.text = userData.partnerMessage
        }
    }
}
