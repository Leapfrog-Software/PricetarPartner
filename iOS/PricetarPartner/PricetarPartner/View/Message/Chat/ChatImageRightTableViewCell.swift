//
//  ChatImageRightTableViewCell.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import UIKit

class ChatImageRightTableViewCell: UITableViewCell {

    @IBOutlet private weak var datetimeLabel: UILabel!
    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var chatImageView: UIImageView!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.userImageView)
        ImageStorage.shared.cancelRequest(imageView: self.chatImageView)
        self.userImageView.image = nil
        self.chatImageView.image = nil
    }
    
    func configure(chatData: ChatData) {
        
        self.datetimeLabel.text = chatData.getDisplayDatetime()
        ImageStorage.shared.fetch(url: UserData.imageUrl(userId: chatData.senderId), imageView: self.userImageView)
        ImageStorage.shared.fetch(url: chatData.imageUrl(), imageView: self.chatImageView)
    }
}
