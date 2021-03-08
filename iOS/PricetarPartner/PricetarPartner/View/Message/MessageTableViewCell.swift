//
//  MessageTableViewCell.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import UIKit

class MessageTableViewCell: UITableViewCell {

    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var unreadCountLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.userImageView)
        self.userImageView.image = nil
    }
    
    func configure(chatGroupData: ChatGroupData) {

        if chatGroupData.userId1 == SaveData.shared.userId {
            ImageStorage.shared.fetch(url: UserData.imageUrl(userId: chatGroupData.userId2), imageView: self.userImageView)
            self.nameLabel.text = FetchUserRequester.shared.query(userId: chatGroupData.userId2)?.nickname ?? ""

            self.unreadCountLabel.isHidden = (chatGroupData.unreadCount2 == 0)
            self.unreadCountLabel.text = (chatGroupData.unreadCount2 > 99) ? "99+" : "\(chatGroupData.unreadCount2)"
        } else {
            ImageStorage.shared.fetch(url: UserData.imageUrl(userId: chatGroupData.userId1), imageView: self.userImageView)
            self.nameLabel.text = FetchUserRequester.shared.query(userId: chatGroupData.userId1)?.nickname ?? ""
            
            self.unreadCountLabel.isHidden = (chatGroupData.unreadCount1 == 0)
            self.unreadCountLabel.text = (chatGroupData.unreadCount1 > 99) ? "99+" : "\(chatGroupData.unreadCount1)"
        }
    }
}
